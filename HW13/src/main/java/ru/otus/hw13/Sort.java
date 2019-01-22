package ru.otus.hw13;

import java.util.*;
import java.util.concurrent.Callable;

@SuppressWarnings("unchecked")
public class Sort<T> {

    private final ThreadPool threadPool = new ThreadPool(4);

    private final Thread thread;

    public Sort() {
        this.thread=Thread.currentThread();
    }

    public List<T> mergSort(List<T> list, Comparator<T> comparator) {
        MergeCollection result = mergSort(new MergeCollection(list), comparator);

        return result.getList();
    }

    public List<T> mergeThreadSort(List<T> list, Comparator<T> comparator, int threads) {
        MergeCollection result = mergeThreadSort(new MergeCollection(list), comparator, threads);

        return result.getList();
    }


    private MergeCollection mergeThreadSort(MergeCollection collection, Comparator<T> comparator, int count) {
        if (collection.isSorted()) return collection;
   //     Map<String, MergeCollection> result = new HashMap<>();
        List<T> list = collection.getList();
        MergeCollection left = new MergeCollection(list.subList(0, list.size() / 2));
        MergeCollection right = new MergeCollection(list.subList(list.size() / 2, list.size()));

        if (!left.isSorted()) {
            left = mergSort(left, comparator);
        }
        if (!right.isSorted()) {
            right = mergSort(right, comparator);
        }
  //      result.put("result", merge(left, right, comparator));

        threadPool.shutdown();

        return merge(left, right, comparator);
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

        MyTask task = new MyTask(left, right, comparator);

        threadPool.execute(task);

        while (!task.isComplete){
            System.out.print("");
        }
        // return merge(left, right, comparator);
        return task.result;
    }


    private MergeCollection merge(MergeCollection left, MergeCollection right, Comparator<T> comparator) {
        Object[] leftArray = left.getList().toArray();
        Object[] rightArray = right.getList().toArray();
        T[] result = (T[]) new Object[leftArray.length + rightArray.length];
        int leftCount = 0;
        int rightCount = 0;
        for (int i = 0; i < result.length; i++) {

            int isleft;
            if (leftCount >= leftArray.length) {
                isleft = -1;
            } else if (rightCount >= rightArray.length) {
                isleft = 1;
            } else {
                isleft = comparator.compare((T) leftArray[leftCount], (T) rightArray[rightCount]);
            }

            if (isleft > 0) {
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

    private class MyTask implements Runnable {
        private MergeCollection result;
        private MergeCollection left;
        private MergeCollection right;
        private Comparator<T> comparator;
        private boolean isComplete=false;

        public MyTask(MergeCollection left, MergeCollection right, Comparator<T> comparator) {
            this.left = left;
            this.right = right;
            this.comparator = comparator;
        }

        @Override
        public void run() {

            System.out.println(Thread.currentThread().getName() +" left "+left);
            System.out.println(Thread.currentThread().getName() +" right "+right);
            this.result =merge(left,right,comparator);
            System.out.println(Thread.currentThread().getName()+ " result " +result);
            isComplete=true;

        }
    }

    @SuppressWarnings("SameParameterValue")
    private class MergeCollection {
        private boolean sorted = false;
        private List<T> list;

        private MergeCollection(List<T> list) {
            this.list = new ArrayList<T>(list);
            ;
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
