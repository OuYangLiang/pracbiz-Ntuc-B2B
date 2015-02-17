@echo off`

if not "%JAVA_HOME%" == "" goto okJavaHome
echo "Please set JAVA_HOME first" 
goto end
:okJavaHome

REM *******************************************************
REM ****** client backend application structure ******************
REM <project_root>
REM <project_root>/bin
REM <project_root>/conf
REM <project_root>/lib
REM client.bat is under <project_root>/bin
REM *******************************************************

cd /d %~dp0

set CLIENT_EXEC_LIB=.;..;..\lib
set CLIENT_EXEC_LIB=%CLIENT_EXEC_LIB%;%JAVA_HOME%\lib
set CLIENT_EXEC_LIB=%CLIENT_EXEC_LIB%;%JAVA_HOME%\jre\lib
set CLIENT_EXEC_LIB=%CLIENT_EXEC_LIB%;%JAVA_HOME%\jre\lib\ext
set CLIENT_EXEC_LIB=%CLIENT_EXEC_LIB%;%JAVA_HOME%\jre\lib\security

set OPT="-Xms256m" "-Xmx512m" "-Djava.ext.dirs=%CLIENT_EXEC_LIB%"

java -cp "%CLIENT_EXEC_LIB%"; %OPT% com.pracbiz.client.startup.Bootstrap %1

:end
pause
cmd