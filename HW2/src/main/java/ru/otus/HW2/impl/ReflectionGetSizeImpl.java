package ru.otus.HW2.impl;

import ru.otus.HW2.Creator;
import ru.otus.HW2.GetSize;
import ru.otus.HW2.util.PrimitiveSize;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class ReflectionGetSizeImpl implements GetSize {
    @Override
    public long getSize(Creator c) {
        try {
           return getPremitiveFields(c.create(),c.create().getClass(),0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }

    }

    private long getPremitiveFields(Object o,Class clazz,long summ) throws IllegalAccessException {
        System.out.println("Object " + o + " Class " + clazz + " summ " + summ);

long size=summ;
//        if (o==null ) {
//            size += 4;
//            return (size/8+1)*8;
//        }




  //      System.out.println(" we get object type " + o.getClass().getName() + " summ = " + summ);

        Field[] fields = clazz.getDeclaredFields();
        for (Field f:fields) {
            if((f.getModifiers() & Modifier.STATIC) == 0){
                if (f.getType().isPrimitive()){
                    System.out.println("field " + f.getName() + " type " + f.getType().getName() + " size " + PrimitiveSize.getSizeByName(f.getType().getName()));
                    size += PrimitiveSize.getSizeByName(f.getType().getName());
                } else {
                        System.out.println("field " + f.getName() + " type " + f.getType().getName() );
                        f.setAccessible(true);
                        getPremitiveFields(f.get(o),f.getType(),size);
                }
            }
        }
        return size % 8 == 0 ? size : ((size / 8) + 1) * 8;
    }


}
