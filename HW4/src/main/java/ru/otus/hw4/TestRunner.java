package ru.otus.hw4;

import ru.otus.hw4.annotations.After;
import ru.otus.hw4.annotations.Before;
import ru.otus.hw4.annotations.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;

public class TestRunner {

    private static Method[] beforeMethods;
    private static Method[] aftereMethods;




    public static <T> void run(Class<T> clazz){
      run(clazz,new Object[0]);
    }

    public static <T> void run(Class<T> clazz, Object... args){
        Method[] testMethods = ReflectionHelper.getAnnotatedMethods(clazz, Test.class);
        beforeMethods = ReflectionHelper.getAnnotatedMethods(clazz, Before.class);
        aftereMethods = ReflectionHelper.getAnnotatedMethods(clazz, After.class);

        for (Method m:testMethods){
            T instance = ReflectionHelper.instantiate(clazz, args);
            Arrays.stream(beforeMethods)
                    .forEach(method -> doMethod(method,instance));
            doMethod(m,instance);
            Arrays.stream(aftereMethods)
                    .forEach(method -> doMethod(method,instance));
        }
    }

    private static void doMethod(Method method,Object instance) {
        ReflectionHelper.callMethod(instance,method.getName());
    }


}
