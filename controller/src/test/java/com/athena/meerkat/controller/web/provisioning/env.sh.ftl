#!/bin/bash

export JAVA_HOME=/usr/lib/jvm/java-1.7.0-openjdk-1.7.0.91-2.6.2.1.el7_1.x86_64
export CATALINA_HOME=/home/meerkat/athena-meerkat-agent/apache-tomcat-7.0.68/apache-tomcat-7.0.68
export CATALINA_BASE=/home/meerkat/Servers/instance1

export CATALINA_OPTS="-server -Xms512m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m"
export CATALINA_OPTS="$CATALINA_OPTS -Denv=product -Denv.servername=instance1"

