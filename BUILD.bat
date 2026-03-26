@echo off
echo ========================================
echo   Silent Tiers Mod - Building...
echo ========================================
echo.
echo Make sure Java 21 is installed!
echo.
call gradlew.bat build
echo.
if exist "build\libs\silent-tiers-mod-1.0.0.jar" (
    echo ========================================
    echo   BUILD SUCCESSFUL!
    echo   Your mod is in: build\libs\
    echo   File: silent-tiers-mod-1.0.0.jar
    echo ========================================
    explorer "build\libs"
) else (
    echo ========================================
    echo   BUILD FAILED - check errors above
    echo ========================================
)
pause
