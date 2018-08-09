#!/bin/bash

MEMORY="-Xms512m -Xmx512m -XshowSettings:vm"

GC_LOG="-Xlog:gc:./logs/gc_pid_%p.log"

#DUMP="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./dumps/"

current_date_time="`date +%Y%m%d%H%M%S`";
echo $current_date_time;

touch GC-Statistics.log

mvn clean package

echo -XX:+UseParallelGC -XX:-UseParallelOldGC
GC='-XX:+UseParallelGC -XX:-UseParallelOldGC'
java $GC $MEMORY $GC_LOG $DUMP  -jar target/HW5.jar

echo -XX:+UseSerialGC
GC=-XX:+UseSerialGC
java $GC $MEMORY $GC_LOG $DUMP  -jar target/HW5.jar

echo -XX:+UseParallelGC
GC=-XX:+UseParallelGC
java $GC $MEMORY $GC_LOG $DUMP  -jar target/HW5.jar

echo -XX:+UseConcMarkSweepGC
GC=-XX:+UseConcMarkSweepGC
java $GC $MEMORY $GC_LOG $DUMP  -jar target/HW5.jar

echo -XX:+UseG1GC
GC=-XX:+UseG1GC
java $GC $MEMORY $GC_LOG $DUMP  -jar target/HW5.jar

current_date_time="`date +%Y%m%d%H%M%S`";
echo $current_date_time;
