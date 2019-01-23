package ru.otus.hw13;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainTest {

    private final int threadCount=8;
    private final int limit=1_000_000;


    @Test
    public void simpleUtilSortTest(){
        List<Integer> list = Stream.generate(()->new Random().nextInt())
                .limit(limit)
                .collect(Collectors.toList());
        List<Integer> before = new ArrayList<>(list);

        Comparator<Integer> comparator=Comparator.comparingInt(Integer::intValue);
        list.sort(comparator);

        before.sort(comparator);
        assertArrayEquals(before.toArray(),list.toArray());
    }

    @Test
    public void simpleMergeSortTest(){
        List<Integer> list = Stream.generate(()->new Random().nextInt())
                .limit(limit)
                .collect(Collectors.toList());
        List<Integer> before = new ArrayList<>(list);

        Comparator<Integer> comparator=Comparator.comparingInt(Integer::intValue);
        Sorter<Integer> mergeSorter = new MergeSort<>();
        List<Integer> result = mergeSorter.sort(list, comparator);

        before.sort(comparator);
        assertArrayEquals(before.toArray(),result.toArray());
    }

    @Test
    public void utilSortTest(){
        List<Integer> list = Stream.generate(()->new Random().nextInt())
                .limit(limit)
                .collect(Collectors.toList());
        List<Integer> before = new ArrayList<>(list);


        UtilSorter<Integer> utilSorter=new UtilSorter<>();
        ThreadSort<Integer> utilSort=new ThreadSort<>(utilSorter);
        Comparator<Integer> comparator=Comparator.comparingInt(Integer::intValue);
        List<Integer> result =utilSort.threadSort(list, comparator,threadCount);
        before.sort(comparator);
        assertArrayEquals(before.toArray(),result.toArray());
    }

    @Test
    public void mergeSortTest(){
        List<Integer> list = Stream.generate(()->new Random().nextInt())
                .limit(limit)
                .collect(Collectors.toList());
        List<Integer> before = new ArrayList<>(list);


        UtilSorter<Integer> mergeSorter=new UtilSorter<>();
        ThreadSort<Integer> mergeSort=new ThreadSort<>(mergeSorter);
        Comparator<Integer> comparator=Comparator.comparingInt(Integer::intValue);
        List<Integer> result =mergeSort.threadSort(list, comparator,threadCount);
        before.sort(comparator);
        assertArrayEquals(before.toArray(),result.toArray());
    }

}