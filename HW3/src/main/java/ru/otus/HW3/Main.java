package ru.otus.HW3;

import java.util.Collections;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        List<String> myArrayList = new MyArrayList<>();
        myArrayList.add("1");
        myArrayList.add("2");
        myArrayList.add("3");
        System.out.println(myArrayList.toString());

        Collections.addAll(myArrayList,"4", "5", "6");
        System.out.println(myArrayList.toString());
        Collections.addAll(myArrayList,"7", "8", "9");
        System.out.println(myArrayList.toString());
        Collections.addAll(myArrayList,"10", "11", "12");
        System.out.println(myArrayList.toString());


        List<String> myArrayListDest = new MyArrayList<>();
        System.out.println(myArrayListDest);
        for(int i = 0; i < 12; i++) {
            myArrayListDest.add("dest" + i);
        }
        Collections.copy(myArrayListDest,myArrayList);
        System.out.println(myArrayListDest);

        List<Integer> myArrayListSort = new MyArrayList<>();
        for(int i = 0; i < 200; i++) {
            myArrayListSort.add(((Double) (Math.random()*1000)).intValue());
        }
        Collections.sort(myArrayListSort, Integer::compareTo);
        System.out.println("sorted:" + myArrayListSort);


 }

}
