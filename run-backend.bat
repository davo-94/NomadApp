@echo off
REM Script para iniciar el backend de NomadApp
REM
REM Uso: ejecutar desde la raíz del proyecto NomadApp

setlocal enabledelayedexpansion

echo.
echo ========================================
echo     INICIANDO NOMADAPP BACKEND
echo ========================================
echo.

REM Verificar que estamos en el directorio correcto
if not exist "backend\build\libs\nomadapp-1.0.0.jar" (
    echo [!] Archivo JAR no encontrado. Compilando...
    call gradlew :backend:build -x test
    if errorlevel 1 (
        echo [ERROR] La compilación del backend falló
        pause
        exit /b 1
    )
)

echo [✓] Backend compilado correctamente
echo.
echo [*] Iniciando servidor en http://localhost:8080
echo.
echo Credenciales Oracle:
echo   - Usuario: admin
echo   - Host: adb.sa-santiago-1.oraclecloud.com:1522
echo   - Wallet: C:\Users\Nicolas\.oracle\wallets
echo.

REM Ejecutar el JAR
java -jar backend\build\libs\nomadapp-1.0.0.jar

if errorlevel 1 (
    echo.
    echo [ERROR] El backend no pudo iniciarse
    echo [!] Verifica que:
    echo    - Oracle está accesible
    echo    - Wallet está en la ruta correcta
    echo    - Las credenciales son válidas
    pause
)
