package ru.otus.hw13;

import java.util.Comparator;
import java.util.List;

public interface Sorter<T> {

    public List<T> sort(List<T> list, Comparator<T> comparator);
}
