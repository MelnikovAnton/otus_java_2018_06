package ru.otus.HW3;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void addGetTest(){
        List<String> myList = new MyArrayList<>();
        myList.add("Hello");
        assertEquals("Hello",myList.get(0));
    }

    @Test
    public void addRemoveTest(){
        List<String> myList = new MyArrayList<>();
        myList.add("Hello");
        myList.remove("Hello");
        assertNull(myList.get(0));
    }

    @Test
    public void addAllTest(){
        List<String> myList = new MyArrayList<>();
        List<String> strings= new ArrayList<>();
        strings.add("Hello");
        myList.addAll(strings);
        assertArrayEquals(myList.toArray(),strings.toArray());
    }

    @Test
    public void addInTest(){
        String[] str= {"first","second","third"};
        List<String> myArray=new MyArrayList<>();
        myArray.add("first");
        myArray.add("third");
        myArray.add(1,"second");
        assertArrayEquals(myArray.toArray(),str);
    }

    @Test
    public void indexOfTest(){
        List<String> myArray=new MyArrayList<>();
        myArray.add("first");
        myArray.add("second");
        myArray.add("third");
        assertEquals(1,myArray.indexOf("second"));
    }

    @Test
    public void lastIndexOfTest(){
        List<String> myArray=new MyArrayList<>();
        myArray.add("first");
        myArray.add("second");
        myArray.add("third");
        myArray.add("second");
        myArray.add("last");
        assertEquals(3,myArray.lastIndexOf("second"));
    }

    @Test
    public void subListTest(){
        List<String> myArray=new MyArrayList<>();
        myArray.add("first");
        myArray.add("second");
        myArray.add("third");
        myArray.add("forth");
        myArray.add("fifth");
        String[] str= {"second","third","forth"};
        List<String> sublist = myArray.subList(1, 3);
        assertArrayEquals(sublist.toArray(),str);
    }

    @Test
    public void containsAllTest(){
        List<String> myArray=new MyArrayList<>();
        myArray.add("first");
        myArray.add("second");
        myArray.add("third");
        myArray.add("forth");
        myArray.add("fifth");
        String[] str= {"forth","second","third","qwe"};
        List<String> arrayList = Arrays.asList(str);

        assertFalse(myArray.containsAll(arrayList));
        myArray.add("qwe");
        assertTrue(myArray.containsAll(arrayList));
    }

    @Test
    public void addAllIndex(){
        List<String> myArray=new MyArrayList<>();
        myArray.add("first");
        myArray.add("second");
        myArray.add("third");
        myArray.add("forth");
        myArray.add("fifth");
        String[] str= {"six","seven","eight","nine","ten","eleven"};
        List<String> arrayList = Arrays.asList(str);
        String[] rez= {"first","second","third","six","seven","eight","nine","ten","eleven","forth","fifth"};
        myArray.addAll(3,arrayList);
        assertArrayEquals(rez,myArray.toArray());

        }
    }
