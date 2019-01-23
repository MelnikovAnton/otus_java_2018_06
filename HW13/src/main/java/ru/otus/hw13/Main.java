package ru.otus.hw13;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)  {



        List<Integer> list = Stream.generate(()->new Random().nextInt())
                .limit(1_000_000)
                .collect(Collectors.toList());
        List<Integer> list2 = new ArrayList<>(list);

        MergeSort<Integer> mergeSorter=new MergeSort<>();
        ThreadSort<Integer> threadSort=new ThreadSort<>(mergeSorter);

        UtilSorter<Integer> utilSorter=new UtilSorter<>();
        ThreadSort<Integer> utilSort=new ThreadSort<>(utilSorter);




        System.out.println("============================");
        System.out.println("Starting Util Thread Sort");
        long start = System.currentTimeMillis();
        List<Integer> result =utilSort.threadSort(list,Comparator.comparingInt(Integer::intValue),8);
        System.out.println("Util Sort finished " + (System.currentTimeMillis()-start));

        System.out.println("============================");
        System.out.println("Starting merge Thread Sort");
        start = System.currentTimeMillis();
        List<Integer> result2 =threadSort.threadSort(list2,Comparator.comparingInt(Integer::intValue),8);
        System.out.println("Merge Sort finished " + (System.currentTimeMillis()-start));



    }
}
