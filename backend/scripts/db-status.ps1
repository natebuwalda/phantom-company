$postgresHome = if ($env:POSTGRES_HOME) { $env:POSTGRES_HOME } else { 'D:\DevTools\postgresql-18.6\pgsql' }
$dataRoot = if ($env:PGDATA) { $env:PGDATA } else { 'D:\PhantomCompany\postgres-data' }

& "$postgresHome\bin\pg_ctl.exe" -D $dataRoot status
