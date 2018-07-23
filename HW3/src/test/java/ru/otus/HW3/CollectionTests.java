package ru.otus.HW3;

import org.junit.Test;

import java.text.CollationKey;
import java.text.Collator;
import java.util.*;

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


        Collections.sort(myList);

        List<Integer> list=new ArrayList<>();
        Collections.addAll(list,intArr);
        Collections.sort(list);

        assertArrayEquals(myList.toArray(),list.toArray());

    }


    @Test
    public void testObjectSort(){
        TestObject[] arr = new TestObject[]{new TestObject(3,"three"),new TestObject(2,"two"),new TestObject(9,"nine")};

        List<TestObject> myList=new MyArrayList<>();
        Collections.addAll(myList,arr);
        Collections.sort(myList, Comparator.comparingInt(o -> o.num));

        List<TestObject> list=new MyArrayList<>();
        Collections.addAll(list,arr);
        Collections.sort(list, Comparator.comparingInt(o -> o.num));

        assertArrayEquals(myList.toArray(),list.toArray());
    }

    private class TestObject{
        private int num;
        private String string;

        public TestObject(int num, String string) {
            this.num = num;
            this.string = string;
        }

        @Override
        public String toString() {
            return "Numder=" + num + " String=" + string;
        }
    }
}
