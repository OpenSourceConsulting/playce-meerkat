#!/bin/bash


if [ $ANT_HOME ] ; then
        EXE_ANT=$ANT_HOME/bin/ant
else
        export ANT_HOME=$(pwd)/apache-ant-1.9.6
fi


echo ANT_HOME: $ANT_HOME


export MRUN_OPTS="$1"

if [ e$2 != "e" ] ; then
    export MRUN_OPTS="$MRUN_OPTS $2"
fi

if [ e$3 != "e" ] ; then
    export MRUN_OPTS="$MRUN_OPTS $3"
fi

echo ant run $MRUN_OPTS

$ANT_HOME/bin/ant -buildfile $MRUN_OPTS

