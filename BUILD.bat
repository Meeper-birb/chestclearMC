@echo off
setlocal EnableExtensions
cd /d "%~dp0"

set "GRADLE_VERSION=6.9.1"
set "GRADLE_DIR=%~dp0tools\gradle-%GRADLE_VERSION%"
set "GRADLE_ZIP=%~dp0tools\gradle-%GRADLE_VERSION%-bin.zip"
set "GRADLE_EXE=%GRADLE_DIR%\bin\gradle.bat"

 echo =====================================
 echo   Clear Render Chests - Build script
 echo =====================================
 echo.
where java >nul 2>&1
if errorlevel 1 (
  echo ERROR: Java was not found.
  echo Install a Java 8 JDK, then run this script again.
  echo Download: https://adoptium.net/temurin/releases/?version=8
  pause
  exit /b 1
)

java -version 2>&1 | findstr /i "1.8" >nul
if errorlevel 1 (
  echo WARNING: Your current Java may not be Java 8.
  echo ForgeGradle for Minecraft 1.16.5 requires Java 8.
  echo If the build fails, install Java 8 and set JAVA_HOME/PATH to it.
  echo.
)

if not exist "%GRADLE_EXE%" (
  echo Gradle %GRADLE_VERSION% not found. Downloading it now...
  if not exist "tools" mkdir "tools"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ProgressPreference='SilentlyContinue'; Invoke-WebRequest 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile '%GRADLE_ZIP%'"
  if errorlevel 1 (
    echo ERROR: Could not download Gradle. Check your internet connection.
    pause
    exit /b 1
  )
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Force '%GRADLE_ZIP%' 'tools'"
  if errorlevel 1 (
    echo ERROR: Could not extract Gradle.
    pause
    exit /b 1
  )
)

echo.
echo Building mod... This may take a while on the first run.
echo.
call "%GRADLE_EXE%" build
if errorlevel 1 (
  echo.
  echo BUILD FAILED. See the error above.
  pause
  exit /b 1
)

echo.
echo BUILD SUCCEEDED!
echo Your JAR should be located in:
echo %~dp0build\libs
explorer "%~dp0build\libs"
pause
