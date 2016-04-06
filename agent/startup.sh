#!/bin/bash


. ./env.sh

AGENT_CLASS=com.athena.meerkat.agent.AgentMain

PID=`ps -ef | grep java | grep "$AGENT_PROCESS_NAME" | awk '{print $2}'`

if [ e$PID != "e" ] ; then
    echo "$AGENT_PROCESS_NAME is already RUNNING..."
    exit;
fi


if [ $JAVA_HOME ] ; then
        EXE_JAVA=$JAVA_HOME/bin/java
else
        EXE_JAVA=java
fi

export JAVA_OPTS="$JAVA_OPTS -Xms256m -Xmx512m"
export JAVA_OPTS="$JAVA_OPTS -Dagent.name=$AGENT_PROCESS_NAME "
export JAVA_OPTS="$JAVA_OPTS -Dspring.config.name=meerkat-agent"
export JAVA_OPTS="$JAVA_OPTS -Djava.library.path=./sigar"

nohup $EXE_JAVA -cp ./conf:lib/* $JAVA_OPTS $AGENT_CLASS > /dev/null 2>&1 &


sleep 1

if [ "$1" != "nolog" ]
then
tail -f logs/agent.log
fi

