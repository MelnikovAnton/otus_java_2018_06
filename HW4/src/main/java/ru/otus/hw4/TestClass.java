package ru.otus.hw4;

import ru.otus.hw4.annotations.After;
import ru.otus.hw4.annotations.Before;
import ru.otus.hw4.annotations.Test;

public class TestClass {


    public TestClass(){
        System.out.println("Constructor");
    }

    @Before
    public void before1(){
        System.out.println("before1");
    }

    @Before
    public void before2(){
        System.out.println("before2");
    }

    @After
    public void after1(){
        System.out.println("After1");
    }
    @After
    public void after2(){
        System.out.println("After2");
    }

    @Test
    public void test1() {
        System.out.println("Test1");
    }

    @Test
    public void test2() {
        System.out.println("Test2");
    }

}
