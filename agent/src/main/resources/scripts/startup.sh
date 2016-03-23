#!/bin/bash

. ./env.sh

cd $CATALINA_HOME/bin
./startup.sh
sleep 1

if [ "$1" != "nolog" ]
then
	tail -f $CATALINA_BASE/logs/catalina.out
fi
