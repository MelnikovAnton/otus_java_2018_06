package ru.otus.hw13;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class ThreadSort<T> {
    private static Logger log = LoggerFactory.getLogger(ThreadSort.class);

    private final Sorter<T> sorter;

    public ThreadSort(Sorter<T> sorter) {
        this.sorter = sorter;
    }

    public List<T> threadSort(List<T> list, Comparator<T> comparator, int count) {

        List<List<T>> parts = splitList(list, count);
        List<Thread> threads = new ArrayList<Thread>();

        for (List<T> item:parts) {
            Thread thread = new Thread(() -> {
                sorter.sort(item,comparator);
            });
            thread.start();
            threads.add(thread);
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        MergeData mergeData = new MergeData(parts, comparator);

        return mergeData.getSortedList();
    }

    private <T> List<List<T>> splitList(List<T> list,int count){
        List<List<T>> chunks = new ArrayList<List<T>>();
        int step = list.size()/count;
        int mod =list.size()%count;
        for (int i =0;i<list.size();i +=step){
            int start;
            int stop = i+step;
            if (mod != 0) {
                start=i;
                i++;
                mod--;
                stop++;
            } else {
                start=i;
            }
            List<T> item = new ArrayList<>(list.subList(start, stop));
            chunks.add(item);
        }

        return chunks;
    }

    @SuppressWarnings("WeakerAccess")
    private class MergeData {
        private final Comparator<T> comparator;
        private final Map<QueweWraper, T> lists;


        public MergeData(List<List<T>> lists, Comparator<T> comparator) {
           this.comparator=comparator;
            List<Queue<T>> queues=new ArrayList<>();
            for (List<T> item : lists) {
                queues.add(new LinkedList<>(item));
            }
            this.lists = queues.stream()
                    .collect(Collectors.toMap(QueweWraper::new, Queue::remove));
        }

        private T getFirst() {

            T first = null;
            QueweWraper key = null;
            for (QueweWraper listWraper : lists.keySet()) {
                while (first == null) {
                    first = lists.get(listWraper);
                    key = listWraper;
                }
                boolean check = comparator.compare(first, lists.get(listWraper)) > 0;
                if (check) {
                    first = lists.get(listWraper);
                    key = listWraper;
                }
            }
            if (key == null) return first;
            if (key.getList().size() == 0) {
                lists.remove(key);
            } else {
                lists.put(key, key.getList().poll());
            }
            return first;
        }

        private List<T> getSortedList() {
            List<T> result = new ArrayList<>();
            T item;
            do {
                item = getFirst();
                if (item != null) result.add(item);
            } while (item != null);

            return result;
        }
    }


    private class QueweWraper {
        private final Queue<T> list;

        public QueweWraper(Queue<T> list) {
            this.list = list;
        }

        public Queue<T> getList() {
            return list;
        }
    }
}
