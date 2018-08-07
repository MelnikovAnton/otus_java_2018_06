package ru.otus.gc;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

@SuppressWarnings("WeakerAccess")
public class GcHelper {

    private static final Collector collector = new Collector();

    public static void registerCollector() {
        NotificationListener listener = (notification, handback) -> {
            if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                GarbageCollectionNotificationInfo gcInfo = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                collector.setGcInfo(gcInfo);
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
