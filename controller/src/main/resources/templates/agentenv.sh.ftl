#!/bin/bash

<#if javaHome??>
export JAVA_HOME=${javaHome}
</#if>
export MEERKAT_AGENT_HOME=${deployDir}/${agentName}
export ANT_HOME="$MEERKAT_AGENT_HOME/apache-ant-1.9.6"
