#!/bin/bash

. ./agentenv.sh

if [ e$1 != "e" ] ; then
    $ANT_HOME/bin/ant -buildfile $1 cmd
    exit;
fi

$ANT_HOME/bin/ant -buildfile cmd.xml cmd
