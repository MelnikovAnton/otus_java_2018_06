package ru.otus.gc;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("WeakerAccess")
public class MemoryLeaker implements MemoryLeakerMBean{

    private int byteSize;

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
            while (true) {
                leakMe(new byte[1_000_000]);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                BadKey key = new BadKey(new byte[byteSize]);
//                map.put(key, new BadValue());
//                //           System.out.println(map.get(key));
//                map.remove(key);
//                i++;
            }

            }catch(OutOfMemoryError e){
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
    public int getCount() {
        return map.size();
    }


    public class BadKey {

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
            Random random = new Random();

            return random.nextBoolean() && random.nextBoolean();
        }
    }

    public class BadValue{
        private ClassLoader cl;
        private byte[] bytes;

        public BadValue(){
            this.cl=Main.class.getClassLoader();
      //      this.bytes=new byte[byteSize];
            leakMe(this);
        }
    }
}
