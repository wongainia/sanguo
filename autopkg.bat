@echo off
 
set /p min=最小id值是:
set /p max=最大id值是: 

for /l %%i in (%min%,1,%max%) do ant -f build.xml -Dchannel.id=%%i pakChannel