#!/bin/bash

. ./env.sh

$ANT_HOME/bin/ant -buildfile cmd.xml cmd
