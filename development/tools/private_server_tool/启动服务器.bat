@rem ---------------------------------------------------------------------------
@rem modeltest editor script for windows by oyxz on 2012-03-22
@rem
@rem ---------------------------------------------------------------------------

@echo off

SETLOCAL ENABLEDELAYEDEXPANSION

set WORK_DIR=%cd%

set CLASSPATH=

for /f "delims=\" %%a in ('dir /b /a-d /o-d "%WORK_DIR%\lib\*.jar"') do (
    set CLASSPATH=!CLASSPATH!;!WORK_DIR!\lib\%%a
)

echo "start ..."

java -Xmx1024m -Xms1024m  -classpath %WORK_DIR%\bin%CLASSPATH% com.core.GameServer

echo "end."

pause
