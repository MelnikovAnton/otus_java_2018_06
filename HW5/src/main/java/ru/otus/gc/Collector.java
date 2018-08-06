package ru.otus.gc;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.lang.management.MemoryUsage;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class Collector {



    public void sendGcInfo(GarbageCollectionNotificationInfo gcInfo){
        String gcName = gcInfo.getGcName();
        String gcAction = gcInfo.getGcAction();
        String gcCause  = gcInfo.getGcCause();

        long startTime = gcInfo.getGcInfo().getStartTime();
        long duration = gcInfo.getGcInfo().getDuration();


        Map<String, MemoryUsage> before = gcInfo.getGcInfo().getMemoryUsageBeforeGc();
        Map<String, MemoryUsage> after = gcInfo.getGcInfo().getMemoryUsageAfterGc();


        float beforeMb = before.values().stream()
                .mapToLong(MemoryUsage::getUsed)
                .sum()/(1024f*1024f);

        float afterMb = after.values().stream()
                .mapToLong(MemoryUsage::getUsed)
                .sum()/(1024f*1024f);


        GCStat.getGc(gcName).addDuration(duration);


        System.out.printf("Before GC: %.1f Mb. After GC: %.1f Mb.\r\n",beforeMb,afterMb);
//        System.out.println("Detail Before:");
//        for (String key:before.keySet()){
//            System.out.println(key +": " + before.get(key).getUsed());
//        }
//        System.out.println("Detail After:");
//        for (String key:after.keySet()){
//            System.out.println(key +": " + after.get(key).getUsed());
//        }
//
//
//
//
//
        System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
    }
}
