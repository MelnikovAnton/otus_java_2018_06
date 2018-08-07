package ru.otus.gc;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.lang.management.MemoryUsage;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("WeakerAccess")
public class Collector {

    public static AtomicBoolean isOOM=new AtomicBoolean(false);

    public void setGcInfo(GarbageCollectionNotificationInfo gcInfo) {
        if (!isOOM.get()) {
            String gcName = gcInfo.getGcName();

            long startTime = gcInfo.getGcInfo().getStartTime();
            long duration = gcInfo.getGcInfo().getDuration();

            Map<String, MemoryUsage> before = gcInfo.getGcInfo().getMemoryUsageBeforeGc();
            Map<String, MemoryUsage> after = gcInfo.getGcInfo().getMemoryUsageAfterGc();


            long beforeMb = before.values().stream()
                    .mapToLong(MemoryUsage::getUsed)
                    .sum();

            long afterMb = after.values().stream()
                    .mapToLong(MemoryUsage::getUsed)
                    .sum();

            GCStat.getGc(gcName).setData(duration, getTime(startTime),(afterMb-beforeMb));

        }

    }

    public static String getTime(long timeDifference1) {
        long timeDifference = timeDifference1 / 1000;
        int h = (int) (timeDifference / (3600));
        int m = (int) ((timeDifference - (h * 3600)) / 60);
        int s = (int) (timeDifference - (h * 3600) - m * 60);

        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
