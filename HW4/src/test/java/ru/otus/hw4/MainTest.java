package ru.otus.hw4;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {

    public MainTest(){
        System.out.println("Constructor");
    }

    @Before
    public void before(){
        System.out.println("before");

    }

    @After
    public void after(){
        System.out.println("After");
    }

    @Test
    public void test1() {
        System.out.println("Test1");
        assert(true);
    }

    @Test
    public void test2() {
        System.out.println("Test2");
    }

    @Before
    public void before2(){
        System.out.println("Before 2");
        assert(true);
    }
}