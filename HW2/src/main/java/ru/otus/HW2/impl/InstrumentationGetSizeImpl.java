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
        return instrumentation.getObjectSize(c.create());
        //Object o=c.create();
     //   return getSize(o,0);

    }



}
