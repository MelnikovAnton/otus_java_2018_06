package ru.otus.gc.classLoaderLeak;

import ru.otus.gc.Collector;
import ru.otus.gc.GCStat;
import ru.otus.gc.Main;

import java.util.*;

public class ClassLoaderLeaker implements ClassLoaderLeakerMBean {

    private boolean stop = false;
    private boolean stopLeak = false;
    private int valueSize = 5 * 1024;
    private int sleepTime = 2;

    private Object look = new Object();

    private Random random = new Random();

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<Object, byte[]> map = new HashMap<>();

    @SuppressWarnings("InfiniteLoopStatement")
    public void leek() {

        try {
            while (!stop) {
                if (random.nextBoolean() || stopLeak) {

                        CustomClassLoader cl = new CustomClassLoader();
                        Class<?> clazz = cl.findClass("ru.otus.gc.classLoaderLeak.SmallObject");
                        Object obj;
                        try {
                            obj = clazz.getConstructor().newInstance();
                            System.out.println("Fake " +map.containsKey(obj));
                            map.put(obj, new byte[valueSize]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            obj = null;
                            cl = null;
                        }

                    sleep(sleepTime);
                } else {
                    SmallObject obj = new SmallObject();
                    System.out.println("Real "+map.containsKey(obj));
                    map.put(obj, new byte[valueSize]);
                    obj = null;
                }

                sleep(sleepTime);

            }

        } catch (OutOfMemoryError e) {
            Collector.isOOM = true;
            clean();
            System.out.println("Time before OOM:" + (System.currentTimeMillis() - Main.start));
            GCStat.saveStatisticToFile();
            System.exit(0);
        }
    }

    private void clean() {
        map.clear();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //=========Implement Methods=========

    @Override
    public void setStop(boolean b) {
        this.stop = b;
    }

    @Override
    public void cleanMemory() {
        clean();
    }

    @Override
    public void setStopLeak(boolean b) {
        this.stopLeak = b;
    }

    @Override
    public void setSize(int i) {
        valueSize = i;
    }

    @Override
    public int getSize() {
        return valueSize;
    }

    @Override
    public void setSleepTime(int i) {
        this.sleepTime = i;
    }


}
