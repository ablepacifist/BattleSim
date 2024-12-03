@echo off
REM Navigate to the directory where the EXE and other files are located
cd /d %~dp0

REM Print the current directory for debugging
echo Current directory: %CD%

REM Run the WarGame EXE
WarGame.exe

pause
