package ru.otus.gc;

import java.util.HashMap;
import java.util.Map;


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

public class Main {
    public static int Byte_Size;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {

            try {
                if (args != null && args.length>0 && Integer.parseInt(args[0])>=0) {
                    Byte_Size = Integer.parseInt(args[0]);
                }else throw new NumberFormatException();
            } catch (NumberFormatException e){
                Byte_Size=8000;
            }


        GcHelper.registerCollector();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Map<MemoryLeaker,Integer> map = new HashMap<MemoryLeaker, Integer>();
       int i=0;
//        while (i<10000) {
//
//            MemoryLeaker key = new MemoryLeaker("key");
//            map.put(key, "value");
//      //      map.remove(key);
//           System.out.println(map.get(key));
//           i++;
//        }
        while (true) {

            MemoryLeaker key = new MemoryLeaker("key");
            map.put(key, i);
 //           System.out.println(map.get(key));
            i++;
        }

    }




}

