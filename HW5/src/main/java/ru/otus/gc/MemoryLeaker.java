package ru.otus.gc;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//import static sun.jvm.hotspot.runtime.PerfMemory.start;

@SuppressWarnings("WeakerAccess")
public class MemoryLeaker implements MemoryLeakerMBean{

    private int byteSize;

    private static final int size = 500_000;

    private static final AtomicInteger minCounter = new AtomicInteger(0);


    private byte[] someMemory = new byte[32*1_000_000];
    Map<BadKey, BadValue> map = new HashMap<>();

    public MemoryLeaker(int byteSize){
        this.byteSize=byteSize;
    }

    @SuppressWarnings({"InfiniteLoopStatement", "MismatchedQueryAndUpdateOfCollection"})
    public void leak() throws OutOfMemoryError{
        new Thread(()->{
            try {
            int i=0;

                BadKey key = new BadKey(new byte[byteSize]);
                map.put(key, new BadValue());
                key = new BadKey(new byte[byteSize]);
                map.put(key, new BadValue());
            while (i<10_000) {
//                leakMe(new byte[1_000_000]);
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                key = new BadKey(new byte[byteSize]);
                map.put(key, new BadValue());
                //           System.out.println(map.get(key));
          //      System.out.println(map.get(key));
           //     map.remove(key);
                i++;
            }

         //   while (i>0){
                for(BadKey b:map.keySet()){
                    System.out.println(map.get(b));
                }

//              i--;
//            }

            }catch(OutOfMemoryError e){
                System.out.println("Time before OOM:" + (System.currentTimeMillis()-Main.start));
                clean();
                GCStat.printStatistic();
                System.out.println("Time before OOM:" + (System.currentTimeMillis()-Main.start));
                System.exit(0);
            }
        }).start();
    }

    static public void leakMe(final Object object) {
        new Thread(() -> {
            Object obj = object;
            for (;;) {
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {}
            }
        }).start();
    }

    public void newLeaker() throws InterruptedException {

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(TimeUnit.MINUTES.toMillis(1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                minCounter.addAndGet(1);
            }}).start();



        List<byte[]> links = new ArrayList<>();
        int idx = 0;
        while (true) {
            links.add(new byte[size + idx * 2]);
            if (idx % (2 + minCounter.get()) == 0) {
                for (int remIdx = links.size() - 1; remIdx > 0 && idx - remIdx < 500; remIdx--) {
                    links.remove(remIdx);
                }
            }
            Thread.sleep(200);
            idx++;
        }
    }





    @Override
    public int getByteSize() {
        return byteSize;
    }

    @Override
    public void setByteSize(int i) {
        this.byteSize=i;

    }

    @Override
    public void clean() {
        someMemory=null;
    }

    @Override
    public long getCount() {
        return map.values().stream()
                .count();
    }


    public class BadKey {


        private final boolean equals= new Random().nextBoolean();

        private byte[] bytes;
        private final ClassLoader cl = Main.class.getClassLoader();

        public BadKey(byte[] bytes){
            this.bytes = bytes;

        }

        @Override
        public int hashCode() {
            return 5;
        }

        @Override
        public boolean equals(Object obj) {
         return false;
        }
    }

    public class BadValue{
        private ClassLoader cl;
        private byte[] bytes;

        public BadValue(){
           this.cl=Main.class.getClassLoader();
            this.bytes=new byte[byteSize];
//            leakMe(this);
        }
    }
}
