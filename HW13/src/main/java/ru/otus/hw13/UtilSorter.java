package ru.otus.hw13;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

public class UtilSorter<T> implements Sorter<T> {
    private static Logger log = LoggerFactory.getLogger(UtilSorter.class);
    @Override
    public List<T> sort(List<T> list, Comparator<T> comparator) {
        list.sort(comparator);
        log.info(Thread.currentThread().getName() + list.toString());
        return list;
    }
}
