@echo off
 
set /p min=��Сidֵ��:
set /p max=���idֵ��: 

for /l %%i in (%min%,1,%max%) do ant -f build.xml -Dchannel.id=%%i pakChannel