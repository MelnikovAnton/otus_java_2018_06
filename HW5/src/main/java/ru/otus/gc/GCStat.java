package ru.otus.gc;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class GCStat {

    private static final String filename="GC-Statistics.log";

    private static String lastGCstart;

    private static final String testName;

    private static int addedCount;
    private static int delittedCount;

    static {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();

        String result=arguments.stream()
                .filter(x->x.contains("-XX:"))
                .map(x->x.substring(4))
                .collect(Collectors.joining("-"));
        long pid = ProcessHandle.current().pid();
        testName=pid + result;
        System.out.println(testName);
    }

    private GCStat(){}

    private final static Map<String,GcItem> list = new HashMap<>();

    public static GcItem getGc(String name){
        if (list.containsKey(name)) return list.get(name);
        else {
            GcItem instance=new GcItem(name);
            list.put(name,instance);
            return instance;
        }
    }

    public static void setAddedCount(int addedCount) {
        GCStat.addedCount = addedCount;
    }

    public static void setDelittedCount(int delittedCount) {
        GCStat.delittedCount = delittedCount;
    }

    public static void saveStatisticToFile(){

        try (FileWriter writer = new FileWriter(filename,true)){
            writer.write(testName + "\n");
            writer.write("time before OOME: " + lastGCstart + "\n");
            writer.write("Added count: " + addedCount + "\n");
            writer.write("Delitted count: " + delittedCount +"\n");
            for (GcItem item:list.values()){
                writer.write(item.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @SuppressWarnings({"WeakerAccess", "unused"})
    public static class GcItem{
        private int gcCount;
        private String gcTime;
        private String name;
        private final List<Long> cleanedList = new ArrayList<>();
        private final List<Long> gcDuration = new ArrayList<>();


        private GcItem(String name){this.name = name;}

        public void setData(long duration, String lastGC, long cleaned){
            gcCount++;
            gcDuration.add(duration);
            lastGCstart = lastGC;
            cleanedList.add(cleaned);

        }

        @SuppressWarnings("deprecation")
        @Override
        public String toString() {
            long totalDuration = gcDuration.stream()
                    .mapToLong(Long::new)
                    .sum();
            double avgDuration = gcDuration.stream()
                    .mapToLong(Long::new)
                    .average().orElse(0.0);

            double totalMb = cleanedList.stream()
                    .mapToDouble(x -> x)
                    .sum() / (1024 * 1024);

            double avgMb = cleanedList.stream()
                    .mapToLong(x -> x)
                    .average()
                    .orElse(0)/ (1024 * 1024);

            return String.format("GC: %s \n" +
                            "Count: %d \n" +
                            "Time: %d ms. \n" +
                            "avgTime: %f ms. \n" +
                            "Total Memory Collected: %f Mb \n" +
                            "avg Memory: %f Mb \n" +
                            "==================================================\n"
                    ,name,gcCount,totalDuration,avgDuration,totalMb,avgMb);
        }
    }
}
