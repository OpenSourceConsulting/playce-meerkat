#!/bin/sh
ps -ef | grep java | grep -v grep | grep "com.athena.meerkat.agent.Starter" | awk {'print "kill -9 " $2'} | sh -x