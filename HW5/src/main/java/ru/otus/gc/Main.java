package ru.otus.gc;



/*
-agentlib:jdwp=transport=dt_socket,address=14000,server=y,suspend=n
-Xms512m
-Xmx512m
-XX:MaxMetaspaceSize=256m
-verbose:gc
-Xlog:gc*:file=./logs/gc_pid_%p.log
-Dcom.sun.management.jmxremote.port=15000
-Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./dumps/
 */


//GCs:
/*
The full list of possible GC algorithm combinations that can work are:

Command Options*	Resulting Collector Combination
-XX:+UseSerialGC	young Copy and old MarkSweepCompact
-XX:+UseG1GC	young G1 Young and old G1 Mixed
-XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+UseAdaptiveSizePolicy	young PS Scavenge old PS MarkSweep with adaptive sizing
-XX:+UseParallelGC -XX:+UseParallelOldGC -XX:-UseAdaptiveSizePolicy	young PS Scavenge old PS MarkSweep, no adaptive sizing
-XX:+UseParNewGC (deprecated in Java 8 and removed in Java 9 - for ParNew see the line below which is NOT deprecated)	young ParNew old MarkSweepCompact
-XX:+UseConcMarkSweepGC -XX:+UseParNewGC	young ParNew old ConcurrentMarkSweep**
-XX:+UseConcMarkSweepGC -XX:-UseParNewGC (deprecated in Java 8 and removed in Java 9)	young Copy old ConcurrentMarkSweep**
*All the combinations listed here will fail to let the JVM start if you add another GC algorithm not listed, with the exception of -XX:+UseParNewGC which is only combinable with -XX:+UseConcMarkSweepGC
**there are many many options for use with -XX:+UseConcMarkSweepGC which change the algorithm, e.g.
-XX:+/-CMSIncrementalMode (deprecated in Java 8 and removed in Java 9) - uses or disables an incremental concurrent GC algorithm
-XX:+/-CMSConcurrentMTEnabled - uses or disables parallel (multiple threads) concurrent GC algorithm
-XX:+/-UseCMSCompactAtFullCollection - uses or disables a compaction when a full GC occurs
Other options equivalent to one of the above:

Command Options Used On Their Own	Equivalent To Entry In Table Above
-XX:+UseParallelGC	-XX:+UseParallelGC -XX:+UseParallelOldGC
-XX:+UseParallelOldGC	-XX:+UseParallelGC -XX:+UseParallelOldGC
-Xincgc (deprecated in Java 8 and removed in Java 9)	-XX:+UseParNewGC -XX:+UseConcMarkSweepGC
-XX:+UseConcMarkSweepGC	-XX:+UseParNewGC -XX:+UseConcMarkSweepGC
no option on most Windows	-XX:+UseG1GC from Java 9, or before that -XX:+UseSerialGC (see also this page)
no option on most Unix	-XX:+UseG1GC from Java 9, or before that -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+UseAdaptiveSizePolicy (see also this page)
-XX:+AggressiveHeap	-XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+UseAdaptiveSizePolicy with a bunch of other options related to sizing memory and threads and how they interact with the OS

 */
public class Main {
    private static int Byte_Size;

    public static void main(String[] args) {

        try {
            if (args != null && args.length > 0 && Integer.parseInt(args[0]) >= 0) {
                Byte_Size = Integer.parseInt(args[0]);
            } else throw new NumberFormatException();
        } catch (NumberFormatException e) {
            Byte_Size = 8000;
        }


        GcHelper.registerCollector();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new MemoryLeaker(Byte_Size).leak();


    }


}

