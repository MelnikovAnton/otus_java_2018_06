package ru.otus.hw15.messageSystem.messageUtils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class DataSetExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getAnnotation(NotForSerialize.class) !=null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
