#!/bin/bash

MEMORY="-Xms512m -Xmx512m -XshowSettings:vm"

GC_LOG=" -verbose:gc -Xlog:gc:./logs/gc_pid_%p.log"

DUMP="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./dumps/"

mvn clean package

#young G1 Young and old G1 Mixed
GC="-XX:+UseG1GC"
java $MEMORY $GC_LOG $DUMP  -jar target/HW5.jar>G1.log


#young Copy and old MarkSweepCompact
GC="-XX:+UseSerialGC"
java $MEMORY $GC $GC_LOG $DUMP  -jar target/HW5.jar>UseSerialGC.log


#young PS Scavenge old PS MarkSweep with adaptive sizing
GC="-XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+UseAdaptiveSizePolicy"
java $MEMORY $GC $GC_LOG $DUMP  -jar target/HW5.jar>UseParallelGC-UseAdaptiveSizePolicy.log

#young PS Scavenge old PS MarkSweep, no adaptive sizing
GC="-XX:+UseParallelGC -XX:+UseParallelOldGC -XX:-UseAdaptiveSizePolicy"
java $MEMORY $GC $GC_LOG $DUMP  -jar target/HW5.jar>UseParallelGC.log

#young ParNew old ConcurrentMarkSweep
GC="-XX:+UseConcMarkSweepGC -XX:+UseParNewGC"
java $MEMORY $GC $GC_LOG $DUMP  -jar target/HW5.jar>UseParallelGC.log




#
#-XX:+UseParNewGC (deprecated in Java 8 and removed in Java 9 - for ParNew see the line below which is NOT deprecated)	young ParNew old MarkSweepCompact
#	**
#-XX:+UseConcMarkSweepGC -XX:-UseParNewGC (deprecated in Java 8 and removed in Java 9)	young Copy old ConcurrentMarkSweep**
