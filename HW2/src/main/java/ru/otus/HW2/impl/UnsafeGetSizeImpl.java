package ru.otus.HW2.impl;

import ru.otus.HW2.Creator;
import ru.otus.HW2.GetSize;
import ru.otus.HW2.util.PrimitiveSize;
import ru.otus.HW2.util.UtilSize;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

public class UnsafeGetSizeImpl implements GetSize {

    private final Unsafe u;

    private final int MIN_SIZE = 16;
    private final int REF_SIZE = 4;
    private static final int NR_BITS = Integer.valueOf(System.getProperty("sun.arch.data.model"));
    private static final int BYTE = 8;
    private static final int WORD = NR_BITS/BYTE;



    public UnsafeGetSizeImpl() {
        System.out.println("NR_BITS/BYTE " + WORD );
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

        try {
            return getSize(creator.create().getClass(),creator.create());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1L;
        }
        //    return sizeOf(creator.create(),0);
    }



    public long getSize(Class clazz,Object o) throws IllegalAccessException {
        if (o==null) return REF_SIZE;
//
//        Class<?> clazz = o.getClass();
        System.out.println(clazz);
      //  System.out.println(clazz.getComponentType());
        if (clazz.isArray()) {
            int arrLength = Array.getLength(o);
            Class<?> itemClass=clazz.getComponentType();

            long itemSize=0;
            System.out.println(itemClass.isPrimitive());
            System.out.println(itemClass.getSimpleName());

           itemSize=PrimitiveSize.getSizeByName(itemClass.getSimpleName());
        if (itemSize==0) {
            itemSize = getSize(itemClass,Array.get(o,0));
        }
            System.out.println("Item size " + itemSize);
            long size = itemSize*arrLength + UtilSize.ARRAY_HEADER;


            System.out.println("item size: " + itemSize + " count: " + arrLength + " size: " + size);

            return roundTo(size,UtilSize.OBJECT_PADDING);
        } else {

            int refSize = 0;
            ArrayList<Field> fields = new ArrayList<>();
      //      Class c = o.getClass();
            while (clazz != Object.class) {
                refSize += UtilSize.REFERENCE_SIZE;
                for (Field f : clazz.getDeclaredFields()) {
                    if ((f.getModifiers() & Modifier.STATIC) == 0) {
                        fields.add(f);
                    }
                }
                clazz = clazz.getSuperclass();
            }

            if (fields.isEmpty()) return MIN_SIZE;
            // get offset
            long maxSize = 0;
            Field maxField = null;
            for (Field f : fields) {
                long offset = u.objectFieldOffset(f);
                if (offset > maxSize) {
                    maxSize = offset;
                    maxField=f;
                }
            }

                    if (maxField != null) {
      //      System.out.println(maxField.getType().getName() + " ==  " +PrimitiveSize.getSizeByName(maxField.getType().getName()) + "   offset = " + maxSize);
                        int maxFieldSize = PrimitiveSize.getSizeByName(maxField.getType().getName());
                        if (maxFieldSize==0){
                            if (maxField!=null){
                                maxField.setAccessible(true);
                                maxSize += getSize(maxField.getType(),maxField.get(o));
                            }else maxSize += REF_SIZE;
                        } else maxSize += maxFieldSize;


            }

      //      maxSize += REF_SIZE;


            return roundTo(maxSize,UtilSize.OBJECT_PADDING);   // padding
        }
    }


   private static long roundTo(long x, int multiple) {
        return ((x + multiple - 1) / multiple) * multiple;
    }

}
