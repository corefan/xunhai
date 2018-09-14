

@echo off
@echo 开始导入指定excel表


cd ..

@rem 
set "EXCEL_FILE=chargeActivity.xlsx"

set "STATIC_PATH=E:/xunhai/development/database/excel/"

cd ./excel_mysql_tool

if not exist "%STATIC_PATH%" mkdir "%STATIC_PATH%"
echo "start import xh_base ..."
java -jar  -Xmx800m data2xls.jar -act 2 -db xh_base -path "%STATIC_PATH%" -file "%EXCEL_FILE%" -gameName ""

echo "-------------end import.---------------"

pause
