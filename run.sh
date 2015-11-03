#!/bin/bash

JVM_ARG='-Xms200m -Xmx200m -XX:+UseG1GC'

if [ "$1" != "" ]; then
    JVM_ARG=$1
fi

echo "Using $JVM_ARG"

nohup java $JVM_ARG -jar schdule-center-1.0-SNAPSHOT-executable.jar > /dev/null &