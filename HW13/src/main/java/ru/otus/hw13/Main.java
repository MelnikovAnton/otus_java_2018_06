package ru.otus.hw13;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = Stream.generate(()->new Random().nextInt(100))
                .limit(10)
                .collect(Collectors.toList());

        Sort<Integer> sort=new Sort<>();
//        List<Integer> result = sort.mergSort(list, Comparator.comparingInt(Integer::intValue));
//
//        result.forEach(System.out::println);

        list.forEach(System.out::println);
        List<Integer> result =sort.mergeThreadSort(list,Comparator.comparingInt(Integer::intValue).reversed(),4);
        result.forEach(System.out::println);




    }


}
