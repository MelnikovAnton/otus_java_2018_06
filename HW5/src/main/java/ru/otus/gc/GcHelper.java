package ru.otus.gc;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.List;
import java.util.Map;

public class GcHelper {

    static final Collector collector = new Collector();

    public static void registerCollector() {
        NotificationListener listener = (notification, handback) -> {
            if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                GarbageCollectionNotificationInfo gcInfo = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                collector.sendGcInfo(gcInfo);
            }
        };
        for (GarbageCollectorMXBean gcBean : ManagementFactory.getGarbageCollectorMXBeans()){
            System.out.println("GC NAME: " + gcBean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcBean;
            emitter.addNotificationListener(listener,null,null);
        }
    }


    private GcHelper() {
    }



}
