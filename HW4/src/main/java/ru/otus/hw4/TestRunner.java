package ru.otus.hw4;

import ru.otus.hw4.annotations.After;
import ru.otus.hw4.annotations.Before;
import ru.otus.hw4.annotations.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;

public class TestRunner {


    private TestRunner(){
    }

    public static <T> void run(Class<T> clazz, Object... args){
        Method[] testMethods = ReflectionHelper.getAnnotatedMethods(clazz, Test.class);
        Method[] beforeMethods = ReflectionHelper.getAnnotatedMethods(clazz, Before.class);
        Method[] aftereMethods = ReflectionHelper.getAnnotatedMethods(clazz, After.class);

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
