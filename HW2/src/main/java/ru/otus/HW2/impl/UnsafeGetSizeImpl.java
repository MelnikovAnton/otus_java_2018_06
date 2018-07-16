package ru.otus.HW2.impl;

import ru.otus.HW2.Creator;
import ru.otus.HW2.GetSize;
import ru.otus.HW2.util.PrimitiveSize;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class UnsafeGetSizeImpl implements GetSize {

    private final Unsafe u;

    private final int MIN_SIZE = 16;
    private final int REF_SIZE = 4;
    private static final int NR_BITS = Integer.valueOf(System.getProperty("sun.arch.data.model"));
    private static final int BYTE = 8;
    private static final int WORD = NR_BITS/BYTE;



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
        return getSize(creator.create());
    //    return sizeOf(creator.create(),0);
    }


//    public long sizeOf(Object o,long summ){
//        //
//        // Get the instance fields of src class
//        //
//if (o == null) return MIN_SIZE + summ;
//        Class<?> src = o.getClass();
//
//        if (src.isArray()) {
//            long arrSize = summ;
//            for (int i = 0; i < Array.getLength(o); i++) {
//                arrSize += sizeOf(Array.get(o, i), summ);
//            }
//            return arrSize;
//        } else {
//        List<Field> instanceFields = new LinkedList<Field>();
//        do{
//            if(src == Object.class) return MIN_SIZE;
//            for (Field f : src.getDeclaredFields()) {
//                if((f.getModifiers() & Modifier.STATIC) == 0){
//                    instanceFields.add(f);
//                }
//            }
//            src = src.getSuperclass();
//        }while(instanceFields.isEmpty());
//        //
//        // Get the field with the maximum offset
//        //
//        long maxOffset = 0;
//        Field maxField = null;
//        for (Field f : instanceFields) {
//            long offset = u.objectFieldOffset(f);
//            if(offset > maxOffset) {
//                maxOffset = offset;
//                maxField = f;
//            }
//        }
//        if (maxField != null) {
//            System.out.println(maxField.getType().getName() + " ==  " +PrimitiveSize.getSizeByName(maxField.getType().getName()) + "   offset = " + maxOffset);
//            maxOffset += PrimitiveSize.getSizeByName(maxField.getType().getName());}
//        return  (maxOffset % 8 == 0 ? maxOffset : ((maxOffset / WORD) + 1) * WORD) + summ;
//    }

    public long getSize(Object o) {
        if (o==null) return REF_SIZE;

        Class<?> clazz = o.getClass();
        System.out.println(clazz);
        if (clazz.isArray()) {
            int arrLength = Array.getLength(o);
            Class<?> itemClass=null;
            try {
            itemClass = Array.get(o, 0).getClass();
            } catch (NullPointerException e) {
                try {
                    itemClass = Class.forName(clazz.toString().substring(2));
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }



            long itemSize=0;
            System.out.println(itemClass.isPrimitive());
            System.out.println(itemClass.getSimpleName());

           itemSize=PrimitiveSize.getSizeByName(itemClass.getSimpleName());
        if (itemSize==0) {
            if (Array.get(o,0)==null) {
                try {
                    getSize(itemClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }else itemSize = getSize(Array.get(o,0));
        }
            System.out.println("Item size " + itemSize);
            long size = itemSize*arrLength + 16;


            System.out.println("item size: " + itemSize + " count: " + arrLength + " size: " + size);

            return size%8==0?size:(size/8+1)*8;
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
                        maxSize += PrimitiveSize.getSizeByName(maxField.getType().getName());}

      //      maxSize += REF_SIZE;


            return (maxSize % 8 == 0 ? maxSize : ((maxSize / 8) + 1) * 8) ;   // padding
        }
    }


}
