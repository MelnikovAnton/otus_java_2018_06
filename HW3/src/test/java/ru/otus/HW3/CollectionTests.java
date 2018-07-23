package ru.otus.HW3;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class CollectionTests {

    @Test
    public void testAddAll(){
        List<String> myList=new ArrayList<>();
        String[] str = new String[]{"one", "two", "three"};
        List<String> string=Arrays.asList(new String[]{"one","two","three"});
        Collections.addAll(myList,str);
        Object[] str1= myList.toArray();
        assertArrayEquals(str,str1);
    }

    @Test
    public void testCopy(){
        String[] str = new String[]{"one", "two", "three"};
        String[] intStr = new String[]{"1", "2", "3"};

        List<String> src=new MyArrayList<>();
        Collections.addAll(src,str);


        List<String> dst=new MyArrayList<>();
        Collections.addAll(dst,intStr);
        System.out.println("before");
        System.out.println("Dst " + dst);
        System.out.println("Src " + src);
        assertArrayEquals(dst.toArray(),intStr);

        Collections.copy(dst,src);

        System.out.println("after");
        System.out.println("Dst " + dst);
        System.out.println("Src " + src);

        assertArrayEquals(dst.toArray(),str);
    }

    @Test
    public void testSort(){
        Integer[] intArr = new Integer[]{5, 4, 3, 2, 1};

        List<Integer> myList=new MyArrayList<>();
        Collections.addAll(myList,intArr);

        System.out.println("Before "+myList);

        Collections.sort(myList);
        System.out.println("After " + myList);
    }
}
