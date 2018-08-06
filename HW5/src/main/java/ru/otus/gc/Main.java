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


import ru.otus.gc.classLoaderLeek.ClassLoaderLeaker;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;

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

 */
public class Main {
    private static int Byte_Size;

    public final static long start = System.currentTimeMillis();



    public static void main(String[] args) throws NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, MalformedObjectNameException {

        try {
            if (args != null && args.length > 0 && Integer.parseInt(args[0]) >= 0) {
                Byte_Size = Integer.parseInt(args[0]);
            } else throw new NumberFormatException();
        } catch (NumberFormatException e) {
            Byte_Size = 8_000;
        }

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=MemoryLeaker");
        MemoryLeaker leaker = new MemoryLeaker(Byte_Size);
        mbs.registerMBean(leaker, name);


        GcHelper.registerCollector();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClassLoaderLeaker сLleaker = new ClassLoaderLeaker();
        System.out.println(сLleaker.getClass().getClassLoader());
        try {
            сLleaker.leek();
        } catch (Exception e) {
            e.printStackTrace();
        }

 //            leaker.leak();


//        try {
//            leaker.newLeaker();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }


}

