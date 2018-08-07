package ru.otus.gc;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GCStat {



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
        long pid = ProcessHandle.current().pid();
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(pid+".log"), "utf-8"))) {
            writer.write("========================START==============================");
            for (GcItem item:list.values()){
                writer.write(item.toString());
            }
            writer.write("==========================END============================");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static class GcItem{
        private int gcCount;
        private long gcDuration;
        private String name;

        private GcItem(String name){this.name = name;}

        public void addDuration(long duration){
            gcCount++;
            gcDuration += duration;
        }

        public int getGcCount() {
            return gcCount;
        }

        public void setGcCount(int gcCount) {
            this.gcCount = gcCount;
        }

        public long getGcDuration() {
            return gcDuration;
        }

        public void setGcDuration(int gcDuration) {
            this.gcDuration = gcDuration;
        }

        @Override
        public String toString() {
            return String.format("GC: %s Count: %d Time: %d",name,gcCount,gcDuration);
        }
    }




}