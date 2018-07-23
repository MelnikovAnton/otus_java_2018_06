package ru.otus.HW3;

public class Main {

    public static void main(String[] args) {
        System.out.println("Test My Array List.....");
        MyArrayList<String> myArrayList=new MyArrayList<>();
        myArrayList.add("Hello");
        System.out.println(myArrayList.get(0));
        myArrayList.remove("Hello");
        System.out.println(myArrayList.get(0));

    }
}
