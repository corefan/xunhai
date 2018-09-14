

@echo off
@echo 开始导入所有excel表

cd ..

set "STATIC_PATH=E:/xunhai/development/database/excel/"

cd ./excel_mysql_tool

if not exist "%STATIC_PATH%" mkdir "%STATIC_PATH%"
echo "start import xh_base ..."
java -jar -Xmx512m data2xls.jar -act 2 -db xh_base -path "%STATIC_PATH%" -gameName ""
echo "-------------end import.---------------"

pause