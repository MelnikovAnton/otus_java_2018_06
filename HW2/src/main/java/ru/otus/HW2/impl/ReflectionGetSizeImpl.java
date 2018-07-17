package ru.otus.HW2.impl;

import ru.otus.HW2.Creator;
import ru.otus.HW2.GetSize;
import ru.otus.HW2.util.PrimitiveSize;
import ru.otus.HW2.util.UtilSize;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import static ru.otus.HW2.util.UtilSize.*;

public class ReflectionGetSizeImpl implements GetSize {



    @Override
    public long getSize(Creator c) {

        Object o = c.create();
        Class<?> clazz = o.getClass();

        try {
            return getSize(clazz,o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long getSize(Class clazz,Object o) throws IllegalAccessException {

        if (clazz.isArray()){
              return getArrSize(clazz,o);
        } else {
            return getObjSize(clazz,o);
        }
    }

    private long getArrSize(Class clazz,Object o) throws IllegalAccessException {
        Class elmClass = clazz.getComponentType();
        int arrLen = Array.getLength(o);
    //    System.out.println("array whith length " + arrLen + " of elements " + elmClass.getName() + " primitive = " + elmClass.isPrimitive());
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
                  size += PrimitiveSize.getSizeByName(Array.get(o, i).getClass().getSimpleName());
            }
        }
        size += ARRAY_HEADER;
        return roundTo(size,OBJECT_PADDING);
    }

    @SuppressWarnings("all")
    private long getObjSize(Class clazz,Object o) throws IllegalAccessException {

        long size=0;
    //    System.out.println(clazz + " , " + o + " , " + padding);
            for (Field f : clazz.getDeclaredFields()) {
                if ((f.getModifiers() & Modifier.STATIC) == 0) {
                    f.setAccessible(true);
                    Class<?> fieldType = f.getType();
                    Object fieldVal = f.get(o);
                    if (!(fieldType.isPrimitive() || boxingClass.contains(fieldType))){
                        if (fieldVal!=null){
                           if (fieldType.isArray())
                               size += getArrSize(fieldType, fieldVal);
                           else size += getObjSize(fieldType, fieldVal);
                        }
                        size += REFERENCE_SIZE;
                    }else size += PrimitiveSize.getSizeByName(fieldType.getSimpleName());
                }
            }

            size += OBJECT_HEADER;
        return roundTo(size,OBJECT_PADDING);
    }

    private static long roundTo(long x, int multiple) {
        return ((x + multiple - 1) / multiple) * multiple;
    }

}
