param(
    [string]$SpecPath = "api-spec.yaml",
    [string]$OutPath = "postman/ms-template.postman_collection.json"
)

$ErrorActionPreference = "Stop"

if (-not (Test-Path $SpecPath)) {
    throw "Spec file not found: $SpecPath"
}

$lines = Get-Content $SpecPath

$inPaths = $false
$currentPath = $null
$currentOp = $null
$expectingTagItem = $false
$inParameters = $false
$inCurrentParameter = $false
$currentParameter = $null
$operations = New-Object System.Collections.Generic.List[object]

function Add-CurrentOperation {
    param([ref]$opRef, [System.Collections.Generic.List[object]]$ops)
    if ($null -ne $opRef.Value) {
        if ([string]::IsNullOrWhiteSpace($opRef.Value.tag)) {
            $opRef.Value.tag = "Misc"
        }
        $ops.Add($opRef.Value)
        $opRef.Value = $null
    }
}

for ($i = 0; $i -lt $lines.Count; $i++) {
    $line = $lines[$i]

    if ($line -match '^paths:\s*$') {
        $inPaths = $true
        continue
    }

    if (-not $inPaths) {
        continue
    }

    if ($line -match '^components:\s*$') {
        Add-CurrentOperation -opRef ([ref]$currentOp) -ops $operations
        break
    }

    if ($line -match '^  (/api/[^:]+):\s*$') {
        Add-CurrentOperation -opRef ([ref]$currentOp) -ops $operations
        $currentPath = $Matches[1]
        $expectingTagItem = $false
        $inParameters = $false
        continue
    }

    if ($line -match '^    (get|post|put|delete|patch|options|head):\s*$') {
        Add-CurrentOperation -opRef ([ref]$currentOp) -ops $operations
        $currentOp = [ordered]@{
            method = $Matches[1].ToUpperInvariant()
            path = $currentPath
            tag = $null
            operationId = $null
            hasBody = $false
            parameters = @()
        }
        $expectingTagItem = $false
        $inParameters = $false
        continue
    }

    if ($null -eq $currentOp) {
        continue
    }

    if ($line -match '^      tags:\s*$') {
        $expectingTagItem = $true
        $inParameters = $false
        continue
    }

    if ($expectingTagItem -and $line -match '^      -\s+(.+?)\s*$') {
        $currentOp.tag = $Matches[1]
        $expectingTagItem = $false
        continue
    }

    if ($line -match '^      operationId:\s*(.+?)\s*$') {
        $currentOp.operationId = $Matches[1]
        $inParameters = $false
        continue
    }

    if ($line -match '^      parameters:\s*$') {
        $inParameters = $true
        continue
    }

    if ($inParameters -and $line -match '^      - name:\s*(.+?)\s*$') {
        # Save previous parameter if exists
        if ($null -ne $currentParameter -and $currentParameter.name) {
            $currentOp.parameters += @($currentParameter)
        }
        $currentParameter = [ordered]@{
            name = $Matches[1]
            in = $null
            type = "string"
            description = $null
            default = $null
            example = $null
            required = $false
        }
        continue
    }

    if ($inParameters -and $null -ne $currentParameter) {
        if ($line -match '^\s+in:\s*(.+?)\s*$') {
            $currentParameter.in = $Matches[1]
            continue
        }
        if ($line -match '^\s+description:\s*(.+?)\s*$') {
            $currentParameter.description = $Matches[1]
            continue
        }
        if ($line -match '^\s+required:\s*(true|false)\s*$') {
            $currentParameter.required = ($Matches[1] -eq "true")
            continue
        }
        if ($line -match '^\s+example:\s*(.+?)\s*$') {
            $currentParameter.example = $Matches[1]
            continue
        }
        if ($line -match '^\s+default:\s*(.+?)\s*$') {
            $currentParameter.default = $Matches[1]
            continue
        }
        if ($line -match '^\s+schema:\s*$') {
            # Look ahead for schema type
            if ($i + 1 -lt $lines.Count -and $lines[$i + 1] -match '^\s+type:\s*(.+?)\s*$') {
                $schemaMatch = $lines[$i + 1] -match '^\s+type:\s*(.+?)\s*$'
                if ($schemaMatch) {
                    $currentParameter.type = $Matches[1]
                }
            }
            continue
        }
        if ($line -match '^      - name:|^      requestBody:|^      responses:' -or $line -match '^    [a-z]+:') {
            if ($currentParameter.name) {
                $currentOp.parameters += @($currentParameter)
            }
            $currentParameter = $null
            if ($line -match '^      requestBody:\s*$') {
                $currentOp.hasBody = $true
            }
            if ($line -match '^      responses:') {
                $inParameters = $false
            }
            continue
        }
    }

    if ($line -match '^      requestBody:\s*$') {
        $currentOp.hasBody = $true
        $inParameters = $false
        continue
    }

    if ($line -match '^      responses:') {
        $inParameters = $false
        continue
    }

    if ($line -notmatch '^      ') {
        $expectingTagItem = $false
        if ($null -ne $currentParameter) {
            if ($currentParameter.name) {
                $currentOp.parameters += @($currentParameter)
            }
            $currentParameter = $null
        }
    }
}

Add-CurrentOperation -opRef ([ref]$currentOp) -ops $operations
if ($null -ne $currentParameter -and $currentParameter.name) {
    if ($null -eq $currentOp.parameters) {
        $currentOp.parameters = @()
    }
    $currentOp.parameters += @($currentParameter)
}

function Get-IdVariableName {
    param([string]$path)
    if ($path -match '/companies/\{id\}$') { return "companyId" }
    if ($path -match '/salesmen/\{id\}$') { return "salesmanId" }
    if ($path -match '/admin-users/\{id\}$') { return "adminUserId" }
    if ($path -match '/posts/\{id\}$') { return "postId" }
    return "id"
}

function Get-BodyTemplateRaw {
        param($op)

        $bodyTemplates = @{
                "POST /api/companies" = @"
{
    "registrationNumber": "{{companyRegistrationNumber}}",
    "emailAddress": "{{companyEmailAddress}}",
    "mobileNumber": "{{companyMobileNumber}}"
}
"@
                "PUT /api/companies/{id}" = @"
{
    "registrationNumber": "{{companyRegistrationNumber}}",
    "emailAddress": "{{companyEmailAddress}}",
    "mobileNumber": "{{companyMobileNumber}}"
}
"@
                "PATCH /api/companies/{id}/status" = @"
{
    "status": "{{companyDecisionStatus}}"
}
"@
                "POST /api/salesmen" = @"
{
    "firstName": "John",
    "middleName": "A",
    "lastName": "Doe",
    "accountNumber": "ACC-001",
    "cifNumber": "CIF-001",
    "idNumber": "ID-001",
    "companyId": {{companyId}},
    "emailAddress": "salesman@example.com",
    "mobileNumber": "+971555020202"
}
"@
                "PUT /api/salesmen/{id}" = @"
{
    "firstName": "John",
    "middleName": "A",
    "lastName": "Doe",
    "accountNumber": "ACC-001",
    "cifNumber": "CIF-001",
    "idNumber": "ID-001"
}
"@
                "POST /api/admin-users" = @"
{
    "firstName": "Admin",
    "middleName": "M",
    "lastName": "User",
    "extensionNumber": "101",
    "emailAddress": "admin@example.com",
    "mobileNumber": "+971555030303"
}
"@
                "PUT /api/admin-users/{id}" = @"
{
    "firstName": "Admin",
    "middleName": "M",
    "lastName": "User",
    "extensionNumber": "101",
    "emailAddress": "admin.updated@example.com",
    "mobileNumber": "+971555030304"
}
"@
        }

        $templateKey = "$($op.method) $($op.path)"
        if ($bodyTemplates.ContainsKey($templateKey)) {
                return $bodyTemplates[$templateKey].Trim()
        }

        return "{}"
}

function Build-RequestObject {
    param($op)

    $rawPath = $op.path
    $pathSegments = @()
    $queryParams = @()

    # Separate path and query parameters
    $pathParams = @()
    $headerParams = @()
    
    if ($null -ne $op.parameters -and $op.parameters.Count -gt 0) {
        foreach ($param in $op.parameters) {
            if ($param.in -eq "path") {
                $pathParams += $param
            }
            elseif ($param.in -eq "query") {
                $queryParams += $param
            }
            elseif ($param.in -eq "header") {
                $headerParams += $param
            }
        }
    }

    # Build path segments
    foreach ($segment in ($rawPath -split '/')) {
        if ([string]::IsNullOrWhiteSpace($segment)) { continue }
        if ($segment -match '^\{.*\}$') {
            # Extract parameter name from {id} or {paramName}
            $paramName = $segment -replace '^\{|\}$', ''
            # Check if this is in path parameters
            $pathParam = $pathParams | Where-Object { $_.name -eq $paramName }
            if ($null -ne $pathParam) {
                $pathSegments += "{{$paramName}}"
            }
            else {
                # Fallback: use variable name mapping
                if ($paramName -eq "id") {
                    $varName = Get-IdVariableName -path $rawPath
                    $pathSegments += "{{${varName}}}"
                }
                else {
                    $pathSegments += "{{$paramName}}"
                }
            }
        }
        else {
            $pathSegments += $segment
        }
    }

    $resolvedPath = ($pathSegments -join '/')
    $rawUrl = "{{baseUrl}}/$resolvedPath"

    $headers = @(
        [ordered]@{ key = "X-Request-Id"; value = "{{X-Request-Id}}" },
        [ordered]@{ key = "Accept-Language"; value = "{{Accept-Language}}" },
        [ordered]@{ key = "X-Tenant-Id"; value = "{{X-Tenant-Id}}" },
        [ordered]@{ key = "USER_ID"; value = "{{USER_ID}}" }
    )

    # Add custom header parameters
    foreach ($headerParam in $headerParams) {
        if ($headerParam.name -ne "USER_ID" -and $headerParam.name -ne "X-Request-Id" -and 
            $headerParam.name -ne "Accept-Language" -and $headerParam.name -ne "X-Tenant-Id") {
            $headers += [ordered]@{ key = $headerParam.name; value = "{{$($headerParam.name)}}" }
        }
    }

    if ($op.hasBody) {
        $headers = @([ordered]@{ key = "Content-Type"; value = "application/json" }) + $headers
    }

    $url = [ordered]@{
        raw = $rawUrl
        host = @("{{baseUrl}}")
        path = $pathSegments
    }

    # Build query array
    if ($queryParams.Count -gt 0) {
        $queryArray = @()
        foreach ($qParam in $queryParams) {
            $value = if ($null -ne $qParam.example) {
                $qParam.example
            }
            elseif ($null -ne $qParam.default) {
                $qParam.default
            }
            else {
                ""
            }
            
            # Remove quotes if present
            $value = $value.Trim('"').Trim("'")

            # Use explicit collection variable for next cursor sample value.
            if ($qParam.name -eq "cursor") {
                $value = "{{nextCursor}}"
            }
            
            $queryArray += [ordered]@{
                key = $qParam.name
                value = $value
                description = $qParam.description
            }
        }
        if ($queryArray.Count -gt 0) {
            $url.query = $queryArray
            # Update raw URL to include query string
            $queryString = ($queryArray | ForEach-Object { "$($_.key)=$($_.value)" }) -join '&'
            $url.raw = "$rawUrl`?$queryString"
        }
    }

    $request = [ordered]@{
        method = $op.method
        header = $headers
        url = $url
    }

    if ($op.hasBody) {
        $request.body = [ordered]@{
            mode = "raw"
            options = [ordered]@{
                raw = [ordered]@{ language = "json" }
            }
            raw = Get-BodyTemplateRaw -op $op
        }
    }

    $displayName = if ([string]::IsNullOrWhiteSpace($op.operationId)) {
        "$($op.method) $($op.path)"
    }
    else {
        $op.operationId
    }

    return [ordered]@{
        name = $displayName
        request = $request
    }
}

$groupedByTag = @{}
foreach ($op in $operations) {
    if (-not $groupedByTag.ContainsKey($op.tag)) {
        $groupedByTag[$op.tag] = New-Object System.Collections.Generic.List[object]
    }
    $groupedByTag[$op.tag].Add((Build-RequestObject -op $op))
}

$folderOrder = @("Companies", "Salesmen", "Admin Users", "Posts")
$folderItems = New-Object System.Collections.Generic.List[object]

foreach ($tag in $folderOrder) {
    if ($groupedByTag.ContainsKey($tag)) {
        $folderItems.Add([ordered]@{
            name = $tag
            item = $groupedByTag[$tag]
        })
        $groupedByTag.Remove($tag)
    }
}

foreach ($remainingTag in ($groupedByTag.Keys | Sort-Object)) {
    $folderItems.Add([ordered]@{
        name = $remainingTag
        item = $groupedByTag[$remainingTag]
    })
}

$collection = [ordered]@{
    info = [ordered]@{
        name = "ms-template API"
        description = "Generated from api-spec.yaml by scripts/generate-postman-from-openapi.ps1"
        schema = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
    }
    variable = @(
        [ordered]@{ key = "baseUrl"; value = "http://localhost:8080" },
        [ordered]@{ key = "nextCursor"; value = "eyJjb21wYW55SWQiOiIxMjAifQ" },
        [ordered]@{ key = "companyId"; value = "1" },
        [ordered]@{ key = "salesmanId"; value = "1" },
        [ordered]@{ key = "adminUserId"; value = "1" },
        [ordered]@{ key = "postId"; value = "1" },
        [ordered]@{ key = "companyRegistrationNumber"; value = "REG-2026-0001" },
        [ordered]@{ key = "companyEmailAddress"; value = "company@example.com" },
        [ordered]@{ key = "companyMobileNumber"; value = "+971555010101" },
        [ordered]@{ key = "companyDecisionStatus"; value = "ACTIVE" },
        [ordered]@{ key = "X-Request-Id"; value = "req-local-001" },
        [ordered]@{ key = "X-Tenant-Id"; value = "SNB" },
        [ordered]@{ key = "USER_ID"; value = "1" },
        [ordered]@{ key = "Accept-Language"; value = "en" }
    )
    item = $folderItems
}

$json = $collection | ConvertTo-Json -Depth 30
$utf8NoBom = New-Object System.Text.UTF8Encoding($false)
[System.IO.File]::WriteAllText((Resolve-Path (Split-Path -Parent $OutPath) | ForEach-Object { Join-Path $_ (Split-Path -Leaf $OutPath) }), $json, $utf8NoBom)

Write-Output "Generated Postman collection: $OutPath"
Write-Output "Operations generated: $($operations.Count)"
