package ru.otus.gc.classLoaderLeek;

import ru.otus.gc.GCStat;
import ru.otus.gc.Main;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ClassLoaderLeaker {

//    private static BigStaticClass b =new BigStaticClass();
    private final Random random = new Random();

    private static final Map<Object,byte[]> map =new HashMap<>();

    public void leek()  {

        try {
        while (true) {
            if (random.nextBoolean()) {
            new Thread(()->{
                CustomClassLoader cl = new CustomClassLoader();
                Class<?> obj = cl.findClass("ru.otus.gc.classLoaderLeek.SmallObject");
                try {
                    map.put(obj.getConstructor().newInstance(),new byte[5*1024]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                obj=null;
                cl=null;

            }).start();

                sleep(5);
            } else {
                SmallObject obj = new SmallObject();

                map.put(obj,new byte[5*1024]);

                obj=null;
            }
//            x/300=1024/159
//                    1932
//                    x/300=2048/71
//                            8610



sleep(5);

        }

    }catch(OutOfMemoryError e){
        clean();
        System.out.println("Time before OOM:" + (System.currentTimeMillis()- Main.start));
        GCStat.printStatistic();
        //e.printStackTrace();
        System.gc();
        System.exit(0);
    }
    }

    private void clean() {
        map.clear();
    }

 private void sleep(long millis){
     try {
         Thread.sleep(millis);
     } catch (InterruptedException e) {
         e.printStackTrace();
     }
 }



}
