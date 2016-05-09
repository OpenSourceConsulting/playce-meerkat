#!/bin/bash

export JAVA_HOME=${javaHome}
export CATALINA_HOME=${catalinaHome}
export CATALINA_BASE=${catalinaBase}

${catalinaOpts}
export CATALINA_OPTS="$CATALINA_OPTS -XX:+HeapDumpOnOutOfMemoryError"
#export CATALINA_OPTS="$CATALINA_OPTS -Dserver.app.name=instance1"
export CATALINA_OPTS="$CATALINA_OPTS -Djava.security.egd=file:/dev/./urandom"

export CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote"
export CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote.ssl=false"
export CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"
export CATALINA_OPTS="$CATALINA_OPTS -Djava.rmi.server.hostname=${rmiHostname}"



