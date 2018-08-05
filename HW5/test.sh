#!/bin/bash

MEMORY="-Xms512m -Xmx512m -XX:MaxMetaspaceSize=256m -XshowSettings:vm"

#GC=""

#GC_LOG=" -verbose:gc -Xlog:gc:./logs/gc_pid_%p.log"

#DUMP="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./dumps/ -XX:OnOutOfMemoryError="kill -3 %p""

mvn clean package

java $MEMORY $GC $GC_LOG $DUMP  -jar target/HW5.jar