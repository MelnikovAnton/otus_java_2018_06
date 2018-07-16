package ru.otus.HW2;

import ru.otus.HW2.impl.*;
import ru.otus.HW2.util.ObjectSizeCalculator;

import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;


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
private static GetSize treeSize = new ReflectionGetSizeImpl();




    public static void main(String... args) throws InterruptedException {
        System.out.println("pid: " + ManagementFactory.getRuntimeMXBean().getName());




//        System.out.println("\nnew Object()");
//        sizePrinter(()->new Object());
//
//        System.out.println("\nnew String(\"\")");
//        sizePrinter(()->new String(""));
//
//        System.out.println("\nnew String(\"Not empty String\")");
//        sizePrinter(()->new String("Not empty String"));
//
//        System.out.println("\nnew String(\"Very long String\")");
//        sizePrinter(()->new String("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"));
//
//        System.out.println("\n(int) 1");
//        sizePrinter(()->(int)1);
//
//        System.out.println("\nnew Integer(1)");
//        sizePrinter(()->new Integer(1));
//
//        System.out.println("\nnew int[2]");
//        sizePrinter(()->new int[2]);
//
//        System.out.println("\nnew int[4]");
//        sizePrinter(()->new int[4]);
//
//        System.out.println("\nnew int[8]");
//        sizePrinter(()->new int[8]);
//
//        System.out.println("\nnew int[16]");
//        sizePrinter(()->new int[16]);
//
//        System.out.println("\nnew int[32]");
//        sizePrinter(()->new int[32]);
//
//        System.out.println("\nnew int[10] with vslues");
//        sizePrinter(()->{
//            int[] rez = new int[10];
//            for (int i=0;i<10;i++){
//                rez[i]=i;
//            }
//            return rez;
//        });
//
//
//        System.out.println("\nnew long[16]");
//        sizePrinter(()->new long[16]);
//
//        System.out.println("\nnew short[7]");
//        sizePrinter(()->new short[7]);


//
//        System.out.println("\nnew MySubClass()");
//        sizePrinter(()->new MySubClass());
//
        System.out.println("\nnew MyClass()");
        sizePrinter(()->new MyClass());
//
//        System.out.println("\nnew BigInteger(\"1\")");
//        sizePrinter(()->new BigInteger("1"));
//
        System.out.println("\nnew MyClass[10] whithout values");
        sizePrinter(()->new MyClass[9]);

        System.out.println("\nnew MyClass[6] ");
        sizePrinter(()->{
            MyClass[] rez = new MyClass[6];
            for (int i=0;i<6;i++){
                rez[i]=new MyClass();
            }
            return rez;
        });

        System.out.println("\nnew Object[1000] whithout values");
        sizePrinter(()->new Object[200]);


        System.out.println("\nnew Object[12]");
        sizePrinter(()->{
            Object[] rez = new Object[12];
            for (int i=0;i<12;i++){
                rez[i]=new Object();
            }

            return rez;
        });

//
//        System.out.println("\nnew HashMap whithout values");
//        sizePrinter(()->new HashMap<String,String>());
//
//        System.out.println("\nnew HashMap whithout values");
//        sizePrinter(()->new HashMap<String,String>());

//        System.out.println("\nnew MyClass[10]");
//        sizePrinter(()->{
//            MyClass[] result = new MyClass[10];
//            for (int i=0;i<10;i++){
//                result[i]=new MyClass();
//            }
//            return result;
//        });



//            Thread.sleep(1000); //wait for 1 sec
  //      }
    }


    private static void sizePrinter(Creator c){
        System.out.println("Array Size =" + arrSize.getSize(c) + " Byte");
        System.out.println("Unsafe Size =" + unsafeSize.getSize(c) + " Byte");
        System.out.println("Instrumentatin Size =" + instrSize.getSize(c) + " Byte");
        System.out.println("Reflection Size =" + treeSize.getSize(c) + " Byte");
        System.out.println("Twitter size calculator " + ObjectSizeCalculator.getObjectSize(c.create()));


    }


    private static class MyClass {
        private byte b = 0;     // +1
        private int i = 0;      // +4
        private long l = 1;     // +8
        private short s1=0; //+2
        private short s2 =0;
        private byte b1 = 0;

    }

    private static class MySubClass extends MyClass{
        private int g=0;
        private long ll=1l;
        private long ll2=0;
        private MyClass myClass = new MyClass();


    }
}
