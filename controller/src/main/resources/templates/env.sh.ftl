#!/bin/bash

export JAVA_HOME=${javaHome}
export CATALINA_HOME=${catalinaHome}
export CATALINA_BASE=${catalinaBase}

export CATALINA_OPTS="-server -Xms512m -Xmx512m"
export CATALINA_OPTS="$CATALINA_OPTS -Denv=product -Denv.servername=instance1"

