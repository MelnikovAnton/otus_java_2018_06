package ru.otus.hw13;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


@SuppressWarnings("unchecked")
public class MergeSort<T> implements Sorter<T>{
    private static Logger log = LoggerFactory.getLogger(MergeSort.class);


    @Override
    public List<T> sort(List<T> list, Comparator<T> comparator) {
        log.info(Thread.currentThread().getName() + list.toString());
        MergeCollection result = mergSort(new MergeCollection(list), comparator);
        List<T> resultList = result.getList();
        list.clear();
        list.addAll(resultList);
        return list;
    }


    private MergeCollection mergSort(MergeCollection collection, Comparator<T> comparator) {
        if (collection.isSorted()) return collection;

        List<T> list = collection.getList();
        MergeCollection left = new MergeCollection(list.subList(0, list.size() / 2));
        MergeCollection right = new MergeCollection(list.subList(list.size() / 2, list.size()));

        if (!left.isSorted()) {
            left = mergSort(left, comparator);
        }
        if (!right.isSorted()) {
            right = mergSort(right, comparator);
        }

        return merge(left, right, comparator);

    }


    private MergeCollection merge(MergeCollection left, MergeCollection right, Comparator<T> comparator) {
        Object[] leftArray = left.getList().toArray();
        Object[] rightArray = right.getList().toArray();
        T[] result = (T[]) new Object[leftArray.length + rightArray.length];
        int leftCount = 0;
        int rightCount = 0;
        for (int i = 0; i < result.length; i++) {

            boolean isleft;
            if (leftCount >= leftArray.length) {
                isleft = false;
            } else if (rightCount >= rightArray.length) {
                isleft = true;
            } else {
                isleft = (comparator.compare((T) leftArray[leftCount], (T) rightArray[rightCount])<0);
            }

            if (isleft) {
                result[i] = (T) leftArray[leftCount];
                leftCount++;
            } else {
                result[i] = (T) rightArray[rightCount];
                rightCount++;
            }
        }
        MergeCollection resultCollection = new MergeCollection(Arrays.asList(result));
        resultCollection.setSorted(true);

        return resultCollection;
    }


    @SuppressWarnings("SameParameterValue")
    private class MergeCollection {
        private boolean sorted = false;
        private List<T> list;

        private MergeCollection(List<T> list) {
            this.list = new ArrayList<T>(list);
            if (list.size()==1) this.sorted=true;
        }

        private boolean isSorted() {
            if (list.size() == 1) this.sorted = true;
            return sorted;
        }

        private List<T> getList() {
            return list;
        }

        private void setSorted(boolean sorted) {
            this.sorted = sorted;
        }

        @Override
        public String toString() {
            return "MergeCollection{" +
                    "sorted=" + sorted +
                    ", list=" + list +
                    '}';
        }
    }
}