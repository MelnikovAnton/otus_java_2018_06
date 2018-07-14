package ru.otus.HW2.impl;

import ru.otus.HW2.Creator;
import ru.otus.HW2.GetSize;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;

public class UnsafeGetSizeImpl implements GetSize {

    private final Unsafe u;

    private final int MIN_SIZE = 16;
    private final int REF_SIZE = 4;


    public UnsafeGetSizeImpl() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            u = (Unsafe) f.get(null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public long getSize(Creator creator) {

        long size=getSize(creator.create(),0);
        return size % 8 == 0 ? size : ((size / 8) + 1) * 8;
    }

    public long getSize(Object o,long summ) {

        Class<?> clazz = o.getClass();

        if (clazz.isArray()) {
            long arrSize = summ;
            for (int i = 0; i < Array.getLength(o); i++) {
                arrSize += getSize(Array.get(o, i), summ);
            }
            return arrSize;
        } else {

            int refSize = 0;
            HashSet<Field> fields = new HashSet<Field>();
            Class c = o.getClass();
            while (c != Object.class) {
                refSize += REF_SIZE;
                for (Field f : c.getDeclaredFields()) {
                    if ((f.getModifiers() & Modifier.STATIC) == 0) {
                        fields.add(f);
                    }
                }
                c = c.getSuperclass();
            }

            if (fields.isEmpty()) return MIN_SIZE;
            // get offset
            long maxSize = 0;
            for (Field f : fields) {
                long offset = u.objectFieldOffset(f);
                if (offset > maxSize) {
                    maxSize = offset;
                }
            }

            maxSize += REF_SIZE;
            maxSize += summ;

            return maxSize;   // padding
        }
    }


}
