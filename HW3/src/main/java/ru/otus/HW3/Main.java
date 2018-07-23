package ru.otus.HW3;

import java.util.Collections;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        List<String> myArrayList = new MyArrayList<>();
        myArrayList.add("1");
        myArrayList.add("2");
        myArrayList.add("3");

        Collections.addAll(myArrayList,"4", "5", "6");
        Collections.addAll(myArrayList,"7", "8", "9");
        Collections.addAll(myArrayList,"10", "11", "12");
        System.out.println("addAll: "+myArrayList.toString());



        List<String> myArrayListSrc = new MyArrayList<>();
        for(int i = 0; i < 12; i++) {
            myArrayListSrc.add("src" + i);
        }
        Collections.copy(myArrayList,myArrayListSrc);
        System.out.println("copy: "+myArrayList);

        List<Integer> myArrayListSort = new MyArrayList<>();
        for(int i = 0; i < 200; i++) {
            myArrayListSort.add(((Double) (Math.random()*2000)).intValue());
        }
        Collections.sort(myArrayListSort, Integer::compareTo);
        System.out.println("sort: " + myArrayListSort);


 }

}
