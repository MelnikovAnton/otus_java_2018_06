package ru.otus.gc.classLoaderLeek;

import ru.otus.gc.Main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CustomClassLoader extends ClassLoader {

    private static final Map<String,byte[]> classes = new HashMap<>();
    static {
//        classes.put("ru.otus.gc.classLoaderLeek.SimpleObject",loadClassData("ru.otus.gc.classLoaderLeek.SimpleObject"));
//        classes.put("ru.otus.gc.classLoaderLeek.BigStaticClass",loadClassData("ru.otus.gc.classLoaderLeek.BigStaticClass"));
        classes.put("ru.otus.gc.classLoaderLeek.SmallObject",loadClassData("ru.otus.gc.classLoaderLeek.SmallObject"));

    }

    @Override
    public Class<?> findClass(String name) {
        byte[] bt = classes.get(name);
        return defineClass(name, bt, 0, bt.length);
    }

    private static byte[] loadClassData(String className) {
        try {
            System.out.println(className + " " + classes.containsKey(className.toString()));
            if (classes.containsKey(className.toString())) return classes.get(className.toString());
            //read class
            InputStream is = Main.class.getClassLoader().getResourceAsStream(className.replace(".", "/") + ".class");
            ByteArrayOutputStream byteSt = new ByteArrayOutputStream();
            //write into byte
            int len = 0;
            try {
                while ((len = is.read()) != -1) {
                    byteSt.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //convert into byte array
            byte[] bt = byteSt.toByteArray();
            classes.put(className.toString(), bt);
            return bt;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
