#!/bin/bash

. ./agentenv.sh

$ANT_HOME/bin/ant -buildfile cmd.xml cmd
