package ru.otus.HW2.impl;

import ru.otus.HW2.Creator;
import ru.otus.HW2.GetSize;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class ObjectGraphGetSize implements GetSize {
    @Override
    public long getSize(Creator c) {
        try {
            return deepObjectGetSize(c.create(),0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int getSizeSpec(Class type, Object object) throws IllegalAccessException {
        if ("java.lang.String".equals(type.getName())) {
            int fieldsCounter = type.getDeclaredFields().length;
            for (int idx = 0; idx < fieldsCounter; idx++) {
                Field field = type.getDeclaredFields()[idx];
                if (field.getName().equals("value")) {
                    field.setAccessible(true);
                    byte[] bytes = (byte[]) field.get(object);
                    return (int) getSizeInternal(bytes);
                }
            }
        } else {
            int fieldsCounter = type.getDeclaredFields().length;
            for (int idx = 0; idx < fieldsCounter; idx++) {
                Field field = type.getDeclaredFields()[idx];
                if (field.getName().equals("SIZE")) {
                    return field.getInt(type);
                }
            }
            if (fieldsCounter > 0 ) {
                int classSize = 0;
                for (int idx = 0; idx < fieldsCounter; idx++) {
                    Field field = type.getDeclaredFields()[idx];
                    if (!"this$0".equals(field.getName())) {
                        Class clazzParent = field.getType();
                        if (clazzParent != null) {
                            field.setAccessible(true);
                            Object objValue = field.get(object);

                            if (objValue==null || objValue.getClass().equals(type)) {
                                throw new IllegalAccessException("infinity loop detected:" + type);
                            }
                            classSize +=getSizeSpec(objValue.getClass(), objValue);
                        }
                    }
                }
                return classSize;
            }
        }
        throw new IllegalAccessException("unknown type:" + type);
    }

    private long deepObjectGetSize(Object object, long accum) throws  IllegalAccessException {
        boolean isArray = object.getClass().isArray();

        if (isArray) {
            long accummArr = accum;
            for (int idx = 0; idx < Array.getLength(object); idx++) {
                accummArr += deepObjectGetSize(Array.get(object, idx), accum);
            }
            return accummArr;
        } else {
            return accum + getSizeSpec(object.getClass(), object);
        }
    }

    private long getSizeInternal(Object object) throws IllegalAccessException {
        return deepObjectGetSize(object, 0);
    }
}
