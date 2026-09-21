$ErrorActionPreference = 'Stop'

$postgresHome = if ($env:POSTGRES_HOME) { $env:POSTGRES_HOME } else { 'D:\DevTools\postgresql-18.6\pgsql' }
$backupRoot = 'D:\PhantomCompany\backups'
$timestamp = Get-Date -Format 'yyyyMMdd-HHmmss'
$backupFile = Join-Path $backupRoot "phantom_company-$timestamp.dump"

New-Item -ItemType Directory -Path $backupRoot -Force | Out-Null

if (-not $env:PHANTOM_DB_PASSWORD) {
    throw 'PHANTOM_DB_PASSWORD is not set. Restart the terminal after setup.'
}

$env:PGPASSWORD = $env:PHANTOM_DB_PASSWORD
try {
    & "$postgresHome\bin\pg_dump.exe" -h 127.0.0.1 -p 5432 -U phantom_app -d phantom_company -F c -f $backupFile
    if ($LASTEXITCODE -ne 0) { throw "pg_dump failed with exit code $LASTEXITCODE" }
} finally {
    Remove-Item Env:PGPASSWORD -ErrorAction SilentlyContinue
}

Write-Output "Backup created: $backupFile"
