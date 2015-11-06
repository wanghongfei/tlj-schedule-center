#!/bin/bash

JVM_ARG='-Xms200m -Xmx200m -XX:+UseG1GC'

echo "Using $JVM_ARG"

nohup java $JVM_ARG -jar sc-exe.jar $1 > /dev/null &