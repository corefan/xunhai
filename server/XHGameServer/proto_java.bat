::protoc.exe -I=proto的输入目录 --java_out=java类输出目录 proto的输入目录包括包括proto文件
::protoc.exe --proto_path=[proto_root] --java_out=[save_root] [*.proto]
::rem 方式1...
::protoc.exe --proto_path=. --java_out=. BS_GC.proto
::rem 方式2...
::protoc.exe -I=. --java_out=pb BS_GC.proto

@echo off
echo 批量导出java的 protocal buffer 协议

echo 开始处理，稍等。。。。
echo ..

set ProtocalFolderName=proto
set OutProtocalFolderName=.\src
for /f "delims=\" %%a in ('dir /b /a-d /o-d "%ProtocalFolderName%\*.proto"') do (
  echo 处理 %%a
  echo %ProtocalFolderName%\%%a
  protoc.exe --proto_path=%ProtocalFolderName% --java_out=%OutProtocalFolderName% %ProtocalFolderName%\%%a
)

echo ..
echo 完成!
pause & exit /b