package ru.otus.HW2;

import ru.otus.HW2.impl.*;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;


/**
 * для сборки:
 * mvn clean install
 *
 * Для запуска:
 * java -javaagent:./target/HW2.jar -jar ./target/HW2.jar
 *
 * Значения для ArrayList у метода reflection меньше реального, так как внутри ArrayList-а находится массив Object[],
 * то есть Reflection посчитает элементы массива равными 16.
 *
 * Судя по всему Instrumentation не лезет в глубь полей и поэтому у всех коллекций возвращает размер объекта без учета размера массива.
 * Аналогичная ситуация и со String. Не учтен byte[].
 *
 * Метод ArrayGetSize не подходит для определения размера строк определенных через "".
 * Так как такие строки хранятся в String Pool и не меняют объем памяти.
 */
@SuppressWarnings({"RedundantStringConstructorCall", "InfiniteLoopStatement"})
public class Main {


    private static GetSize arrSize = new ArrayGetSizeImpl();
    private static GetSize instrSize = new InstrumentationGetSizeImpl();
    private static GetSize reflectSize = new ReflectionGetSizeImpl();

    private static List<Factory> factories = new ArrayList<>();

    static {
        factories.add(new Factory(Object::new,"new Object()"));
        factories.add(new Factory(() -> new Object[10],"new Object[10] without values"));
        factories.add(new Factory(() -> {
            Object[] rez = new Object[10];
            for (int i = 0; i < 10; i++) {
                rez[i] = new Object();
            }
            return rez;
        },"new Object[10] with values"));
        factories.add(new Factory(() -> new String(""),"new String(\"\")"));
        factories.add(new Factory(() -> new String("Not empty String"),"new String(\"Not empty String\")"));
        factories.add(new Factory(() -> arrSize.toString(),"arrSize.toString()"));
        factories.add(new Factory(() -> new int[2],"new int[2]"));
        factories.add(new Factory(() -> new int[10],"new int[10]"));
        factories.add(new Factory(MyClass::new,"MyClass::new"));
        factories.add(new Factory(() -> new MyClass[10],"new MyClass[10] without values"));
        factories.add(new Factory(() -> {
            MyClass[] rez = new MyClass[10];
            for (int i = 0; i < 10; i++) {
                rez[i] = new MyClass();
            }
            return rez;
        },"new MyClass[10] with values"));

        factories.add(new Factory(() -> new ArrayList<MyClass>(),"new ArrayList<MyClass>()"));
        factories.add(new Factory(() -> {
            List<MyClass> rez = new ArrayList<MyClass>();
            for (int i = 0; i < 10; i++) {
               rez.add(new MyClass());
            }
            return rez;
        },"ArrayList<MyClass> with 10 values"));
        factories.add(new Factory(() -> {
            List<MyClass> rez = new ArrayList<MyClass>();
            for (int i = 0; i < 20; i++) {
                rez.add(new MyClass());
            }
            ((ArrayList<MyClass>) rez).trimToSize();
            return rez;
        },"ArrayList<MyClass> with 20 values"));

        factories.add(new Factory(() -> new ArrayList(),"new ArrayList()"));
        factories.add(new Factory(() -> {
            List<Object> rez = new ArrayList<Object>();
            for (int i = 0; i < 10; i++) {
                rez.add(new Object());
            }
            return rez;
        },"ArrayList<Object> with 10 values"));
        factories.add(new Factory(() -> {
            List<Object> rez = new ArrayList<Object>();
            for (int i = 0; i < 20; i++) {
                rez.add(new Object());
            }
            return rez;
        },"ArrayList<Object> with 20 values"));

    }


    public static void main(String... args)  {
        System.out.println("pid: " + ManagementFactory.getRuntimeMXBean().getName());
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        for (Factory f:factories) {
            sizePrinter(f);
        }
    }


    private static void sizePrinter(Factory f) {
        Creator c = f.getCreator();
        System.out.println(f.getDescription());
        System.out.println("Array Size =" + arrSize.getSize(c) + " Byte");
        System.out.println("Instrumentation Size =" + instrSize.getSize(c) + " Byte");
        System.out.println("Reflection Size =" + reflectSize.getSize(c) + " Byte");
        System.out.println();
    }


    private static class MyClass {
        private byte b = 0;     // +1
        private int i = 0;      // +4

    }
}
