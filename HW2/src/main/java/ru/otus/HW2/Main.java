package ru.otus.HW2;

import ru.otus.HW2.impl.ArrayGetSizeImpl;
import ru.otus.HW2.impl.InstrumentationGetSizeImpl;
import ru.otus.HW2.impl.UnsafeGetSizeImpl;
import sun.misc.Unsafe;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;

/**
 * VM options -Xmx512m -Xms512m
 * -XX:+UseCompressedOops //on
 * -XX:-UseCompressedOops //off
 * <p>
 * Runtime runtime = Runtime.getRuntime();
 * long mem = runtime.totalMemory() - runtime.freeMemory();
 * <p>
 * System.gc()
 * <p>
 * jconsole, connect to pid
 *
 * java -javaagent:./target/HW2.jar -jar ./target/HW2.jar
 */
@SuppressWarnings({"RedundantStringConstructorCall", "InfiniteLoopStatement"})
public class Main {


    private static GetSize arrSize= new ArrayGetSizeImpl();
    private static GetSize unsafeSize = new UnsafeGetSizeImpl();
    private static GetSize instrSize = new InstrumentationGetSizeImpl();


    public static void main(String... args) throws InterruptedException {
        System.out.println("pid: " + ManagementFactory.getRuntimeMXBean().getName());




        System.out.println("\nnew Object()");
        sizePrinter(()->new Object());

        System.out.println("\nnew String(\"\")");
        sizePrinter(()->new String(""));

        System.out.println("\nnew MyClass()");
        sizePrinter(()->new MyClass());

        System.out.println("\nnew String(\"Not empty String\")");
        sizePrinter(()->new String("Not empty String"));

        System.out.println("\n(int) 1");
        sizePrinter(()->(int)1);

        System.out.println("\nnew Integer(1)");
        sizePrinter(()->new Integer(1));

        System.out.println("\nnew int[5]");
        sizePrinter(()->new int[5]);

        System.out.println("\nnew MySubClass()");
        sizePrinter(()->new MySubClass());

        System.out.println("\nnew MyClass[10]");
        sizePrinter(()->{
            MyClass[] result = new MyClass[5];
            for (int i=0;i<5;i++){
                result[i]=new MyClass();
            }
            return result;
        });



//            Thread.sleep(1000); //wait for 1 sec
  //      }
    }


    private static void sizePrinter(Creator c){
        System.out.println("Array Size =" + arrSize.getSize(c) + " Byte");
        System.out.println("Unsafe Size =" + unsafeSize.getSize(c) + " Byte");
        System.out.println("Instrumentatin Size =" + instrSize.getSize(c) + " Byte");
    }


    private static class MyClass {
        private byte b = 0;     // +1
        private int i = 0;      // +4
        private long l = 1;     // +8
  //      private byte b1=0;
    }

    private static class MySubClass extends MyClass{
        private int g=0;
        private long ll=1l;
        private MyClass myClass;


    }
}
