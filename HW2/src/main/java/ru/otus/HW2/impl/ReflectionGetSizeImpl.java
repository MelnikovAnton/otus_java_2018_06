package ru.otus.HW2.impl;

import ru.otus.HW2.Creator;
import ru.otus.HW2.GetSize;
import ru.otus.HW2.util.ObjectSizeCalculator;
import ru.otus.HW2.util.PrimitiveSize;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ReflectionGetSizeImpl implements GetSize {

    private Field[] referenceFields;

    @Override
    public long getSize(Creator c) {
        return 0;

    }

//    public long getClassSize(Class clazz,Object o){
//        {
//            long fieldsSize = 0;
//            List<Field> Fields = new ArrayList<>();
//            for (Field f : clazz.getDeclaredFields()) {
//                if ((f.getModifiers() & Modifier.STATIC) == 0) {
//                    continue;
//                }
//                Class type = f.getType();
//                if (type.isPrimitive()) {
//                    fieldsSize += PrimitiveSize.getSizeByName(type.getName());
//                } else {
//                    f.setAccessible(true);
//                    f.get(o);
//                    fieldsSize += referenceSize;
//                }
//            }
//            final Class<?> superClass = clazz.getSuperclass();
//            if (superClass != null) {
//                final ObjectSizeCalculator.ClassSizeInfo superClassInfo = classSizeInfos.getUnchecked(superClass);
//                fieldsSize += roundTo(superClassInfo.fieldsSize, superclassFieldPadding);
//                referenceFields.addAll(Arrays.asList(superClassInfo.referenceFields));
//            }
//            this.fieldsSize = fieldsSize;
//            this.objectSize = roundTo(objectHeaderSize + fieldsSize, objectPadding);
//            this.referenceFields = referenceFields.toArray(
//                    new Field[referenceFields.size()]);
//        }
//    }


}
