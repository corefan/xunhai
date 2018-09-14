echo off

set VERSION_DIR=\\192.168.1.200\share\version\XHGameServer
set CUR_DIR=%cd%

echo start publish bin......
xcopy /s /y %VERSION_DIR%\bin %CUR_DIR%\bin\
echo publish bin end.

echo start publish conf......
xcopy /s /y %VERSION_DIR%\conf %CUR_DIR%\conf\
echo publish conf end.

echo start publish lib......
xcopy /s /y %VERSION_DIR%\lib %CUR_DIR%\lib\
echo publish lib end.
