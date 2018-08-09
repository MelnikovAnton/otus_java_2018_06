package ru.otus.gc;

import ru.otus.gc.data.BadKey;
import ru.otus.gc.data.GoodKey;
import ru.otus.gc.data.Parent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Leaker implements LeakerMBean {

    private byte[] memmory = new byte[32 * 1024 * 1024];

    private final Object look = new Object();

    private boolean loop = true;

    private int sleep = 10;

    private int size = 10 * 1024;

    private final Map<Parent, Byte[]> map = new HashMap<>();
    private List<Byte[]> list = new ArrayList<>();


    public void leak() {
        sleep(10000);
        new Thread(() -> {
            sleep(1000);
            int i = 0;
            while (loop) {
                if (i < list.size() / 2) {
                    synchronized (look) {
                        list.set(i, null);
                        i++;
                    }
                }
                sleep(2 * sleep);
            }
        }).start();


        try {
            while (loop) {
                synchronized (look) {
                    list.add(new Byte[size]);
                }
                sleep(sleep);

            }
        } catch (OutOfMemoryError e) {
            memmory = null;
            Collector.isOOM = true;
            loop = false;
            clean();
            System.out.println("Time before OOM:" + (System.currentTimeMillis() - Main.start));
            GCStat.saveStatisticToFile();
            System.exit(0);
        }
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //======= Impemementation ===========
    @Override
    public void clean() {
        map.clear();
    }

    @Override
    public boolean getLoop() {
        return loop;
    }

    @Override
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    public int getSleep() {
        return sleep;
    }

    @Override
    public void setSeep(int sleep) {
        this.sleep = sleep;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }


}
