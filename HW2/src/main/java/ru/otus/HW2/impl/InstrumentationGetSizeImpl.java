package ru.otus.HW2.impl;

import ru.otus.HW2.Creator;
import ru.otus.HW2.GetSize;
import ru.otus.HW2.util.PrimitiveSize;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;

import static ru.otus.HW2.util.UtilSize.*;

public class InstrumentationGetSizeImpl implements GetSize {

    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    @Override
    public long getSize(Creator c) {

        Object o = c.create();
        Class<?> clazz = o.getClass();

        if (clazz.isArray()){
           return getArrSize(clazz,o);
            } else return getObjSize(clazz,o);

    }


    private long getArrSize(Class clazz,Object o){
        Class elmClass = clazz.getComponentType();
        int arrLen = Array.getLength(o);
        long size=0;
        for (int i=0;i<arrLen;i++){
            if (!(elmClass.isPrimitive() || boxingClass.contains(elmClass))) {
                if (Array.get(o, i) != null) {
                    if (Array.get(o, i).getClass().isArray())
                        size += getArrSize(Array.get(o, i).getClass(), Array.get(o, i));
                    else size += getObjSize(elmClass, Array.get(o, i));
                }
                size += REFERENCE_SIZE;
            } else {
                size += PrimitiveSize.getSizeByName(elmClass.getSimpleName());
            }
        }
        size += ARRAY_HEADER;
        return roundTo(size,OBJECT_PADDING);
    }

    private long getObjSize(Class clazz,Object o){
        return instrumentation.getObjectSize(o);
    }

    private static long roundTo(long x, int multiple) {
        return ((x + multiple - 1) / multiple) * multiple;
    }
}
