$base = "c:\Users\riyas\Desktop\F_Tech\ms-template\src\main\java\com\snb\ms"

# ── Stage 1: Create target directories ────────────────────────────────────────
"company","salesman","adminuser","companysalesman","shared" | ForEach-Object {
    New-Item -ItemType Directory -Force -Path "$base\$_" | Out-Null
}
Write-Host "Directories created."

# ── Stage 1b: Copy files with package declaration update ───────────────────────
# Each entry: sourcePath, destPath, oldPackage, newPackage
$fileMoves = @(
    # entity → new home
    @("entity\BaseEntity.java",                       "shared\BaseEntity.java",                          "com.snb.ms.entity",      "com.snb.ms.shared"),
    @("entity\Users.java",                            "shared\Users.java",                               "com.snb.ms.entity",      "com.snb.ms.shared"),
    @("entity\Company.java",                          "company\Company.java",                            "com.snb.ms.entity",      "com.snb.ms.company"),
    @("entity\Salesman.java",                         "salesman\Salesman.java",                          "com.snb.ms.entity",      "com.snb.ms.salesman"),
    @("entity\AdminUser.java",                        "adminuser\AdminUser.java",                        "com.snb.ms.entity",      "com.snb.ms.adminuser"),
    @("entity\CompanySalesman.java",                  "companysalesman\CompanySalesman.java",            "com.snb.ms.entity",      "com.snb.ms.companysalesman"),
    # dto → new home
    @("dto\BaseResponseDTO.java",                     "shared\BaseResponseDTO.java",                     "com.snb.ms.dto",         "com.snb.ms.shared"),
    @("dto\ErrorInfo.java",                           "shared\ErrorInfo.java",                           "com.snb.ms.dto",         "com.snb.ms.shared"),
    @("dto\UsersDto.java",                            "shared\UsersDto.java",                            "com.snb.ms.dto",         "com.snb.ms.shared"),
    @("dto\UsersRequest.java",                        "shared\UsersRequest.java",                        "com.snb.ms.dto",         "com.snb.ms.shared"),
    @("dto\CompanyDto.java",                          "company\CompanyDto.java",                         "com.snb.ms.dto",         "com.snb.ms.company"),
    @("dto\CompanyCreateRequest.java",                "company\CompanyCreateRequest.java",               "com.snb.ms.dto",         "com.snb.ms.company"),
    @("dto\CompanyUpdateRequest.java",                "company\CompanyUpdateRequest.java",               "com.snb.ms.dto",         "com.snb.ms.company"),
    @("dto\CompanyRequest.java",                      "company\CompanyRequest.java",                     "com.snb.ms.dto",         "com.snb.ms.company"),
    @("dto\SalesmanDto.java",                         "salesman\SalesmanDto.java",                       "com.snb.ms.dto",         "com.snb.ms.salesman"),
    @("dto\SalesmanCreateRequest.java",               "salesman\SalesmanCreateRequest.java",             "com.snb.ms.dto",         "com.snb.ms.salesman"),
    @("dto\SalesmanUpdateRequest.java",               "salesman\SalesmanUpdateRequest.java",             "com.snb.ms.dto",         "com.snb.ms.salesman"),
    @("dto\SalesmanRequest.java",                     "salesman\SalesmanRequest.java",                   "com.snb.ms.dto",         "com.snb.ms.salesman"),
    @("dto\AdminUserDto.java",                        "adminuser\AdminUserDto.java",                     "com.snb.ms.dto",         "com.snb.ms.adminuser"),
    @("dto\AdminUserCreateRequest.java",              "adminuser\AdminUserCreateRequest.java",           "com.snb.ms.dto",         "com.snb.ms.adminuser"),
    @("dto\AdminUserUpdateRequest.java",              "adminuser\AdminUserUpdateRequest.java",           "com.snb.ms.dto",         "com.snb.ms.adminuser"),
    @("dto\AdminUserRequest.java",                    "adminuser\AdminUserRequest.java",                 "com.snb.ms.dto",         "com.snb.ms.adminuser"),
    @("dto\CompanySalesmanDto.java",                  "companysalesman\CompanySalesmanDto.java",         "com.snb.ms.dto",         "com.snb.ms.companysalesman"),
    @("dto\CompanySalesmanRequest.java",              "companysalesman\CompanySalesmanRequest.java",     "com.snb.ms.dto",         "com.snb.ms.companysalesman"),
    # repository → new home
    @("repository\UsersRepository.java",              "shared\UsersRepository.java",                     "com.snb.ms.repository",  "com.snb.ms.shared"),
    @("repository\CompanyRepository.java",            "company\CompanyRepository.java",                  "com.snb.ms.repository",  "com.snb.ms.company"),
    @("repository\SalesmanRepository.java",           "salesman\SalesmanRepository.java",               "com.snb.ms.repository",  "com.snb.ms.salesman"),
    @("repository\AdminUserRepository.java",          "adminuser\AdminUserRepository.java",             "com.snb.ms.repository",  "com.snb.ms.adminuser"),
    @("repository\CompanySalesmanRepository.java",    "companysalesman\CompanySalesmanRepository.java", "com.snb.ms.repository",  "com.snb.ms.companysalesman"),
    # mapper → new home
    @("mapper\UsersMapper.java",                      "shared\UsersMapper.java",                         "com.snb.ms.mapper",      "com.snb.ms.shared"),
    @("mapper\CompanyMapper.java",                    "company\CompanyMapper.java",                      "com.snb.ms.mapper",      "com.snb.ms.company"),
    @("mapper\SalesmanMapper.java",                   "salesman\SalesmanMapper.java",                   "com.snb.ms.mapper",      "com.snb.ms.salesman"),
    @("mapper\AdminUserMapper.java",                  "adminuser\AdminUserMapper.java",                 "com.snb.ms.mapper",      "com.snb.ms.adminuser"),
    @("mapper\CompanySalesmanMapper.java",            "companysalesman\CompanySalesmanMapper.java",     "com.snb.ms.mapper",      "com.snb.ms.companysalesman"),
    # service → new home
    @("service\UsersService.java",                    "shared\UsersService.java",                        "com.snb.ms.service",     "com.snb.ms.shared"),
    @("service\UserProvisioningService.java",         "shared\UserProvisioningService.java",             "com.snb.ms.service",     "com.snb.ms.shared"),
    @("service\CompanyService.java",                  "company\CompanyService.java",                     "com.snb.ms.service",     "com.snb.ms.company"),
    @("service\SalesmanService.java",                 "salesman\SalesmanService.java",                  "com.snb.ms.service",     "com.snb.ms.salesman"),
    @("service\AdminUserService.java",                "adminuser\AdminUserService.java",                "com.snb.ms.service",     "com.snb.ms.adminuser"),
    @("service\CompanySalesmanService.java",          "companysalesman\CompanySalesmanService.java",    "com.snb.ms.service",     "com.snb.ms.companysalesman"),
    # controller → new home
    @("controller\CompanyController.java",            "company\CompanyController.java",                  "com.snb.ms.controller",  "com.snb.ms.company"),
    @("controller\SalesmanController.java",           "salesman\SalesmanController.java",               "com.snb.ms.controller",  "com.snb.ms.salesman"),
    @("controller\AdminUserController.java",          "adminuser\AdminUserController.java",             "com.snb.ms.controller",  "com.snb.ms.adminuser")
)

foreach ($move in $fileMoves) {
    $srcFile = Join-Path $base $move[0]
    $dstFile = Join-Path $base $move[1]
    $oldPkg  = "package $($move[2]);"
    $newPkg  = "package $($move[3]);"
    if (Test-Path $srcFile) {
        $content = [System.IO.File]::ReadAllText($srcFile, [System.Text.Encoding]::UTF8)
        $content = $content.Replace($oldPkg, $newPkg)
        [System.IO.File]::WriteAllText($dstFile, $content, [System.Text.Encoding]::UTF8)
        Write-Host "  Copied : $($move[0])  ->  $($move[1])"
    } else {
        Write-Warning "  MISSING: $srcFile"
    }
}

# ── Stage 2: Update imports across all moved files + exception + config ────────
$importMap = [ordered]@{
    "com.snb.ms.entity.BaseEntity"                    = "com.snb.ms.shared.BaseEntity"
    "com.snb.ms.entity.Users"                         = "com.snb.ms.shared.Users"
    "com.snb.ms.entity.Company"                       = "com.snb.ms.company.Company"
    "com.snb.ms.entity.Salesman"                      = "com.snb.ms.salesman.Salesman"
    "com.snb.ms.entity.AdminUser"                     = "com.snb.ms.adminuser.AdminUser"
    "com.snb.ms.entity.CompanySalesman"               = "com.snb.ms.companysalesman.CompanySalesman"
    "com.snb.ms.dto.BaseResponseDTO"                  = "com.snb.ms.shared.BaseResponseDTO"
    "com.snb.ms.dto.ErrorInfo"                        = "com.snb.ms.shared.ErrorInfo"
    "com.snb.ms.dto.UsersDto"                         = "com.snb.ms.shared.UsersDto"
    "com.snb.ms.dto.UsersRequest"                     = "com.snb.ms.shared.UsersRequest"
    "com.snb.ms.dto.CompanyDto"                       = "com.snb.ms.company.CompanyDto"
    "com.snb.ms.dto.CompanyCreateRequest"             = "com.snb.ms.company.CompanyCreateRequest"
    "com.snb.ms.dto.CompanyUpdateRequest"             = "com.snb.ms.company.CompanyUpdateRequest"
    "com.snb.ms.dto.CompanyRequest"                   = "com.snb.ms.company.CompanyRequest"
    "com.snb.ms.dto.SalesmanDto"                      = "com.snb.ms.salesman.SalesmanDto"
    "com.snb.ms.dto.SalesmanCreateRequest"            = "com.snb.ms.salesman.SalesmanCreateRequest"
    "com.snb.ms.dto.SalesmanUpdateRequest"            = "com.snb.ms.salesman.SalesmanUpdateRequest"
    "com.snb.ms.dto.SalesmanRequest"                  = "com.snb.ms.salesman.SalesmanRequest"
    "com.snb.ms.dto.AdminUserDto"                     = "com.snb.ms.adminuser.AdminUserDto"
    "com.snb.ms.dto.AdminUserCreateRequest"           = "com.snb.ms.adminuser.AdminUserCreateRequest"
    "com.snb.ms.dto.AdminUserUpdateRequest"           = "com.snb.ms.adminuser.AdminUserUpdateRequest"
    "com.snb.ms.dto.AdminUserRequest"                 = "com.snb.ms.adminuser.AdminUserRequest"
    "com.snb.ms.dto.CompanySalesmanDto"               = "com.snb.ms.companysalesman.CompanySalesmanDto"
    "com.snb.ms.dto.CompanySalesmanRequest"           = "com.snb.ms.companysalesman.CompanySalesmanRequest"
    "com.snb.ms.repository.UsersRepository"           = "com.snb.ms.shared.UsersRepository"
    "com.snb.ms.repository.CompanyRepository"         = "com.snb.ms.company.CompanyRepository"
    "com.snb.ms.repository.SalesmanRepository"        = "com.snb.ms.salesman.SalesmanRepository"
    "com.snb.ms.repository.AdminUserRepository"       = "com.snb.ms.adminuser.AdminUserRepository"
    "com.snb.ms.repository.CompanySalesmanRepository" = "com.snb.ms.companysalesman.CompanySalesmanRepository"
    "com.snb.ms.mapper.UsersMapper"                   = "com.snb.ms.shared.UsersMapper"
    "com.snb.ms.mapper.CompanyMapper"                 = "com.snb.ms.company.CompanyMapper"
    "com.snb.ms.mapper.SalesmanMapper"                = "com.snb.ms.salesman.SalesmanMapper"
    "com.snb.ms.mapper.AdminUserMapper"               = "com.snb.ms.adminuser.AdminUserMapper"
    "com.snb.ms.mapper.CompanySalesmanMapper"         = "com.snb.ms.companysalesman.CompanySalesmanMapper"
    "com.snb.ms.service.UsersService"                 = "com.snb.ms.shared.UsersService"
    "com.snb.ms.service.UserProvisioningService"      = "com.snb.ms.shared.UserProvisioningService"
    "com.snb.ms.service.CompanyService"               = "com.snb.ms.company.CompanyService"
    "com.snb.ms.service.SalesmanService"              = "com.snb.ms.salesman.SalesmanService"
    "com.snb.ms.service.AdminUserService"             = "com.snb.ms.adminuser.AdminUserService"
    "com.snb.ms.service.CompanySalesmanService"       = "com.snb.ms.companysalesman.CompanySalesmanService"
    "com.snb.ms.controller.CompanyController"         = "com.snb.ms.company.CompanyController"
    "com.snb.ms.controller.SalesmanController"        = "com.snb.ms.salesman.SalesmanController"
    "com.snb.ms.controller.AdminUserController"       = "com.snb.ms.adminuser.AdminUserController"
}

$scanDirs = @("company","salesman","adminuser","companysalesman","shared","exception","config") |
    ForEach-Object { "$base\$_" } |
    Where-Object { Test-Path $_ }

$allNewFiles = $scanDirs | ForEach-Object { Get-ChildItem $_ -Filter "*.java" }

Write-Host "`nUpdating imports..."
foreach ($file in $allNewFiles) {
    $content = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)
    $modified = $false
    foreach ($old in $importMap.Keys) {
        if ($content.Contains($old)) {
            $content = $content.Replace($old, $importMap[$old])
            $modified = $true
        }
    }
    if ($modified) {
        [System.IO.File]::WriteAllText($file.FullName, $content, [System.Text.Encoding]::UTF8)
        Write-Host "  Imports: $($file.Name)"
    }
}

# ── Stage 3: Add missing imports (were same-package, now cross-package) ────────
function Add-Import($filePath, $importLine) {
    if (-not (Test-Path $filePath)) { Write-Warning "File not found for import injection: $filePath"; return }
    $content = [System.IO.File]::ReadAllText($filePath, [System.Text.Encoding]::UTF8)
    if ($content.Contains($importLine)) { return }   # already present
    # Insert right after the package declaration (first semicolon)
    $semi = $content.IndexOf(";")
    if ($semi -lt 0) { Write-Warning "No semicolon in $filePath"; return }
    $nl = if ($content.Contains("`r`n")) { "`r`n" } else { "`n" }
    $content = $content.Substring(0, $semi + 1) + $nl + "import $importLine;" + $content.Substring($semi + 1)
    [System.IO.File]::WriteAllText($filePath, $content, [System.Text.Encoding]::UTF8)
    Write-Host "  AddImport '$importLine' -> $(Split-Path $filePath -Leaf)"
}

Write-Host "`nInjecting cross-package imports..."
# Entities: BaseEntity & Users were same-package (entity); now BaseEntity+Users → shared
Add-Import "$base\company\Company.java"                       "com.snb.ms.shared.BaseEntity"
Add-Import "$base\company\Company.java"                       "com.snb.ms.shared.Users"
Add-Import "$base\salesman\Salesman.java"                     "com.snb.ms.shared.BaseEntity"
Add-Import "$base\salesman\Salesman.java"                     "com.snb.ms.shared.Users"
Add-Import "$base\adminuser\AdminUser.java"                   "com.snb.ms.shared.BaseEntity"
Add-Import "$base\adminuser\AdminUser.java"                   "com.snb.ms.shared.Users"
# CompanySalesman: Company → company pkg, Salesman → salesman pkg
Add-Import "$base\companysalesman\CompanySalesman.java"       "com.snb.ms.company.Company"
Add-Import "$base\companysalesman\CompanySalesman.java"       "com.snb.ms.salesman.Salesman"
# DTOs: BaseResponseDTO was same-package (dto); now → shared
Add-Import "$base\company\CompanyDto.java"                    "com.snb.ms.shared.BaseResponseDTO"
Add-Import "$base\salesman\SalesmanDto.java"                  "com.snb.ms.shared.BaseResponseDTO"
Add-Import "$base\adminuser\AdminUserDto.java"                "com.snb.ms.shared.BaseResponseDTO"
Add-Import "$base\companysalesman\CompanySalesmanDto.java"    "com.snb.ms.shared.BaseResponseDTO"
# UsersDto: same shared pkg → no injection needed
# UsersDto also extends BaseResponseDTO - same package, fine

# ── Stage 4: Delete old directories ───────────────────────────────────────────
Write-Host "`nRemoving old package directories..."
@("entity","dto","repository","mapper","service","controller") | ForEach-Object {
    $dir = "$base\$_"
    if (Test-Path $dir) {
        Remove-Item -Recurse -Force $dir
        Write-Host "  Removed: $_"
    }
}

Write-Host "`nMigration complete!"
