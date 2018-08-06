package ru.otus.gc.classLoaderLeek;

import ru.otus.gc.GCStat;
import ru.otus.gc.Main;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ClassLoaderLeaker {

//    private static BigStaticClass b =new BigStaticClass();
    private final Random random = new Random();

    private static final Map<Object,byte[]> map =new HashMap<>();

    public void leek() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, InterruptedException {

        try {
        while (true) {
            if (random.nextBoolean()) {
            new Thread(()->{
                CustomClassLoader cl = new CustomClassLoader();
                Class<?> obj = cl.findClass("ru.otus.gc.classLoaderLeek.SmallObject");
                try {
                    map.put(obj.getConstructor().newInstance(),new byte[1024]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//            System.out.println(ob.getClassLoader());
                obj=null;
                cl=null;

            }).start();

     //       System.out.println(map.size());


            } else {
                SmallObject obj = new SmallObject();
                map.put(obj,new byte[1024]);
             //   System.out.println(obj.getClass().getClassLoader());
                obj=null;
            }

Thread.sleep(0,200);
        }
//            Set<Object> set = map.keySet();
//            System.out.println(set.size());
//            set.forEach(o->{
//                System.out.println(o + " : " + map.get(o).length + " " +o.getClass().getClassLoader());
//            });
//
//        for (Object o:map.keySet()){
//
//        }
//        map.clear();
//            System.out.println("+++++++++++++");
//            for (Object o:map.keySet()){
//                System.out.println(o + " : " + map.get(o).length);
//            }

    }catch(OutOfMemoryError e){

        clean();
        System.out.println("Time before OOM:" + (System.currentTimeMillis()- Main.start));
        GCStat.printStatistic();
        e.printStackTrace();
        System.gc();
   //     Thread.sleep(20_000);
        System.exit(0);
    }
    }

    private void clean() {

        map.clear();
    }

    private Object getObject(CustomClassLoader cl) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        Class<?> clazz = cl.findClass("ru.otus.gc.classLoaderLeek.SimpleObject");
       Class<?> clazz1 = cl.findClass("ru.otus.gc.classLoaderLeek.BigStaticClass");


    //    clazz.getMethod("m1").invoke(obj);
        return clazz.getConstructor().newInstance();
    }



}
