@echo off

if "%ANT_HOME%"=="" goto setDefaultAntHome

:setDefaultAntHome
rem %~dp0 is expanded pathname of the current script under NT
set ANT_HOME=%~dp0apache-ant-1.9.6

echo ANT_HOME: %ANT_HOME%


set "MRUN_OPTS=%1"

if not "%2" == "" set "MRUN_OPTS=%MRUN_OPTS% %2"

if not "%3" == "" set "MRUN_OPTS=%MRUN_OPTS% %3"

echo ant run %MRUN_OPTS%

%ANT_HOME%\bin\ant -buildfile %MRUN_OPTS%
