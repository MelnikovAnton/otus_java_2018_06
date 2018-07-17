package ru.otus.HW2.util;

import java.util.HashSet;
import java.util.Set;

public class UtilSize {
    public static final int ARRAY_HEADER = 16;
    public static final int OBJECT_HEADER = 12;
    public static final int OBJECT_PADDING = 8;
    public static final int REFERENCE_SIZE = 4;

    public static final Set<Class> boxingClass = new HashSet<>();

    static {
        boxingClass.add(Byte.class);
        boxingClass.add(Boolean.class);
        boxingClass.add(Short.class);
        boxingClass.add(Character.class);
        boxingClass.add(Integer.class);
        boxingClass.add(Float.class);
        boxingClass.add(Long.class);
        boxingClass.add(Double.class);
    }


    //Uncompressed

//    public static final int ARRAY_HEADER = 24;
//    public static final int OBJECT_HEADER = 26;
//    public static final int OBJECT_PADDING = 8;
//    public static final int REFERENCE_SIZE = 8;


    private static long roundTo(long x, int multiple) {
        return ((x + multiple - 1) / multiple) * multiple;
    }

}
