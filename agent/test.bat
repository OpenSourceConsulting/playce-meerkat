@echo off

if "%ANT_HOME%"=="" goto setDefaultAntHome

:setDefaultAntHome
rem %~dp0 is expanded pathname of the current script under NT
set ANT_HOME=%~dp0apache-ant-1.9.6

echo ANT_HOME: %ANT_HOME%


%ANT_HOME%\bin\ant test
