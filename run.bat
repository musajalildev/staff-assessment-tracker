@echo off
setlocal

set RUN_CLIENT=false
set RUN_SERVER=false

:parseArgs

if "%~1"=="" goto endParse

if "%~1"=="--client" set RUN_CLIENT=true
if "%~1"=="--server" set RUN_SERVER=true

shift
goto parseArgs
:endParse

REM ===== Client =====
if "%RUN_CLIENT%"=="true" (
    echo Running React client...
    pushd client
    start "" npm run dev
    popd
)

REM ===== Server =====
if "%RUN_SERVER%"=="true" (
    echo Running Spring Boot server...
    pushd server
    start "" mvnw.cmd spring-boot:run
    popd
)
