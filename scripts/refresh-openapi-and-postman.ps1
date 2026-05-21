param(
    [string]$ApiDocsUrl = "http://localhost:8080/v3/api-docs.yaml",
    [string]$SpecPath = "api-spec.yaml",
    [string]$PostmanOutPath = "postman/ms-template.postman_collection.json"
)

$ErrorActionPreference = "Stop"

Write-Host "Refreshing OpenAPI spec from $ApiDocsUrl"

try {
    Invoke-WebRequest -Uri $ApiDocsUrl -OutFile $SpecPath
} catch {
    throw "Failed to fetch OpenAPI spec from '$ApiDocsUrl'. Ensure the app is running and reachable before running this task."
}

if (-not (Test-Path $SpecPath)) {
    throw "OpenAPI spec was not created at '$SpecPath'."
}

Write-Host "Generating Postman collection from $SpecPath"
& powershell -ExecutionPolicy Bypass -File "scripts/generate-postman-from-openapi.ps1" -SpecPath $SpecPath -OutPath $PostmanOutPath

if ($LASTEXITCODE -ne 0) {
    throw "Postman generation failed."
}

Write-Host "OpenAPI and Postman refresh completed: $SpecPath, $PostmanOutPath"
