#!/bin/bash

. ./env.sh

export JAVA_OPTS="-server -Xms512m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m"
cd $CATALINA_HOME/bin
./startup.sh
sleep 1

tail -f $CATALINA_BASE/logs/catalina.out
