package ru.otus.hw13;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sorter<T> {
    private static Logger log = LoggerFactory.getLogger(Sorter.class);
    private final List<Thread> threadPool = new ArrayList<>();


    public Sorter(int count,List<T> list, Comparator<T> comparator) {
        int lengt=list.size()/count;

        for (int i = 0; i<list.size(); i += lengt){
            List<T> sublist = list.subList(i, (i < list.size() ? i : list.size()));
            threadPool.add(new Thread(()->{
                sort(sublist,comparator);
                sublist.forEach(s->log.info(s.toString()));

            }));
        }
//        threadPool=Stream.generate(()->new Thread(()->sort(list,comparator)))
//                .limit(count)
//                .collect(Collectors.toList());
    }

    public List<T> sort(List<T> list, Comparator<T> comparator){
        list.sort(comparator);
               return list;
    }

    public List<T> threadSort(List<T> list, Comparator<T> comparator, int count) throws InterruptedException {
        int size=list.size();
        int lengt=size/count;
        for (int i = 0; i<size; i += lengt){
            List<T> sublist = list.subList(i, (i < size ? i : size));
            Thread thread = new Thread(() -> {
                sort(sublist, comparator);
            });
            thread.start();

            sublist.forEach(System.out::println);
        }
        Thread.currentThread().join();
        System.out.println("===========");
        return list;
    }

}
