package ru.otus.gc;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GCStat {


    private static String lastGCstart;

    private static final String testName;

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

    public static void printStatistic(){


        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("GC-Statistics.log")))) {
            writer.write(testName + "\n");
            writer.write("time before OOME: " + lastGCstart + "\n");
            for (GcItem item:list.values()){
                writer.write(item.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




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

        @Override
        public String toString() {
            long totalDuration = gcDuration.stream()
                    .mapToLong(Long::new)
                    .sum();
            double avgDuration = gcDuration.stream()
                    .mapToLong(Long::new)
                    .average().orElse(0.0);

            long totalMb = cleanedList.stream()
                    .mapToLong(x -> x)
                    .sum() / (1024 * 1024);

            long avgMb = cleanedList.stream()
                    .mapToLong(x -> x)
                    .sum() / (1024 * 1024);

            return String.format("GC: %s \n" +
                            "Count: %d \n" +
                            "Time: %d ms. \n" +
                            "avgTime: %f ms. \n" +
                            "Total Memory Collected: %d Mb \n" +
                            "avg Memory: %d Mb \n" +
                            "==================================================\n"
                    ,name,gcCount,totalDuration,avgDuration,totalMb,avgMb);
        }
    }
}
