#!/bin/bash


if [ $ANT_HOME ] ; then
        EXE_ANT=$ANT_HOME/bin/ant
else
        export ANT_HOME=$(pwd)/apache-ant-1.9.6
fi


echo ANT_HOME: $ANT_HOME


$ANT_HOME/bin/ant -buildfile $1 deploy-agent

