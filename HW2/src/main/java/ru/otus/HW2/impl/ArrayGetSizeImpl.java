package ru.otus.HW2.impl;

import ru.otus.HW2.*;

public class ArrayGetSizeImpl implements GetSize {


    @Override
    public long getSize(Creator c) {
        return getSizeByArray(c);
    }

    private long getSizeByArray(Creator c){
        int size = 2_000_000;

        long mem = getMem();
   //     System.out.println("Mem: " + mem);

        Object[] array = new Object[size];

        long mem2 = getMem();
       System.out.println("Ref size: " + (mem2 - mem) / array.length);

        for (int i = 0; i < array.length; i++) {
            array[i] = c.create();
        }

        long mem3 = getMem();
        long objectSize=(mem3 - mem2) / array.length;
  //      System.out.println("Element size: " + objectSize);
        return objectSize;

    }


    private long getMem()  {
        System.gc();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
