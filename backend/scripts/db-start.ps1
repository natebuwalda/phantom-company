$ErrorActionPreference = 'Stop'

$postgresHome = if ($env:POSTGRES_HOME) { $env:POSTGRES_HOME } else { 'D:\DevTools\postgresql-18.6\pgsql' }
$dataRoot = if ($env:PGDATA) { $env:PGDATA } else { 'D:\PhantomCompany\postgres-data' }
$logFile = 'D:\PhantomCompany\logs\postgresql.log'

& "$postgresHome\bin\pg_ctl.exe" -D $dataRoot status *> $null
if ($LASTEXITCODE -eq 0) {
    Write-Output 'PostgreSQL is already running on 127.0.0.1:5432.'
    exit 0
}

$scheduledTask = Get-ScheduledTask -TaskName 'PhantomCompanyPostgres' -ErrorAction SilentlyContinue
if ($scheduledTask) {
    Start-ScheduledTask -TaskName 'PhantomCompanyPostgres'
} else {
    $arguments = "-D `"$dataRoot`" -l `"$logFile`" -o `"-h 127.0.0.1 -p 5432`" start"
    Start-Process -FilePath "$postgresHome\bin\pg_ctl.exe" -ArgumentList $arguments -WindowStyle Hidden
}

for ($attempt = 0; $attempt -lt 40; $attempt++) {
    & "$postgresHome\bin\pg_isready.exe" -h 127.0.0.1 -p 5432 -q
    if ($LASTEXITCODE -eq 0) {
        Write-Output 'PostgreSQL started on 127.0.0.1:5432.'
        exit 0
    }
    Start-Sleep -Milliseconds 250
}

throw "PostgreSQL did not become ready. See $logFile"
