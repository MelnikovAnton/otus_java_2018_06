package ru.otus.HW2.impl;

import ru.otus.HW2.Creator;
import ru.otus.HW2.GetSize;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;

public class InstrumentationGetSizeImpl implements GetSize {

    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    @Override
    public long getSize(Creator c) {
        Object o=c.create();
        return getSize(o,0);
    }

    private long getSize(Object o,long summ){
        Class<?> clazz = o.getClass();

        if (clazz.isArray()){
            long arrSize=summ;
            for(int i = 0; i<Array.getLength(o); i++){
                arrSize += getSize(Array.get(o, i),summ);
            }
            return arrSize;
        } else  {
            return summ+instrumentation.getObjectSize(o);
        }


    }
}
