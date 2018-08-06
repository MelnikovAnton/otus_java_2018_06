package ru.otus.gc.classLoaderLeek;

import ru.otus.gc.GCStat;
import ru.otus.gc.Main;
import ru.otus.gc.MemoryLeaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class ClassLoaderLeaker {

//    private static BigStaticClass b =new BigStaticClass();

 //   private static final Set<Object> set=new HashSet<>();

    public void leek() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, InterruptedException {

        try {
        while (true) {
            CustomClassLoader cl = new CustomClassLoader();
            Object obj = getObject(cl);
            Class<?> ob = cl.findClass("ru.otus.gc.classLoaderLeek.SmallObject");
    //        set.add(ob.getConstructor().newInstance());
//            System.out.println(ob.getClassLoader());


            obj=null;
        }
    }catch(OutOfMemoryError e){

        clean();
        System.out.println("Time before OOM:" + (System.currentTimeMillis()- Main.start));
        GCStat.printStatistic();
        Thread.sleep(Long.MAX_VALUE);
        System.exit(0);
    }
    }

    private void clean() {

  //      set.clear();
    }

    private Object getObject(CustomClassLoader cl) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        Class<?> clazz = cl.findClass("ru.otus.gc.classLoaderLeek.SimpleObject");
       Class<?> clazz1 = cl.findClass("ru.otus.gc.classLoaderLeek.BigStaticClass");


    //    clazz.getMethod("m1").invoke(obj);
        return clazz.getConstructor().newInstance();
    }



}
