set MEMORY=-Xms512m -Xmx512m -XshowSettings:vm

set GC_LOG=-Xlog:gc:./logs/gc_pid_%p.log

set DUMP=

call mvn clean package

echo -XX:+UseG1GC
set GC=-XX:+UseG1GC
call java %MEMORY% %GC_LOG% %DUMP%  -jar target/HW5.jar

echo -XX:+UseSerialGC
set GC=-XX:+UseSerialGC
call java %MEMORY% %GC% %GC_LOG% %DUMP%  -jar target/HW5.jar


echo -XX:+UseParallelGC
set GC=-XX:+UseParallelGC
call java %MEMORY% %GC% %GC_LOG% %DUMP%  -jar target/HW5.jar

echo -XX:+UseParallelGC -XX:-UseParallelOldGC
set GC=-XX:+UseParallelGC -XX:-UseParallelOldGC
call java %MEMORY% %GC% %GC_LOG% %DUMP%  -jar target/HW5.jar

echo -XX:+UseConcMarkSweepGC
set GC=-XX:+UseConcMarkSweepGC
call java %MEMORY% %GC% %GC_LOG% %DUMP%  -jar target/HW5.jar

