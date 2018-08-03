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
                .sum();

        float afterMb = after.values().stream()
                .mapToLong(MemoryUsage::getUsed)
                .sum();

        System.out.printf("Before GC: %.1f Mb. After GC: %.1f Mb.\r\n",beforeMb/1_000_000,afterMb/1_000_000);




        System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
    }
}
