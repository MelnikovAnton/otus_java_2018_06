package ru.otus.gc;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.lang.management.MemoryUsage;
import java.util.Map;

public class Collector {

    public void sendGcInfo(GarbageCollectionNotificationInfo gcInfo){
        String gcName = gcInfo.getGcName();
        String gcAction = gcInfo.getGcAction();
        String gcCause  = gcInfo.getGcCause();

        long startTime = gcInfo.getGcInfo().getStartTime();
        long duration = gcInfo.getGcInfo().getDuration();
        if (gcCause.contains("Failure")){
            System.out.println("FAIL!!!!!");
        }


        Map<String, MemoryUsage> before = gcInfo.getGcInfo().getMemoryUsageAfterGc();
        Map<String, MemoryUsage> after = gcInfo.getGcInfo().getMemoryUsageAfterGc();

        System.out.println("==============before===============");

        for (String b:before.keySet()){
            System.out.println(b + " == " + before.get(b).getUsed());
        }
        System.out.println("==============after===============");
        for (String a:after.keySet()){
            System.out.println(a + " == " + before.get(a).getUsed());
        }



        System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
    }
}
