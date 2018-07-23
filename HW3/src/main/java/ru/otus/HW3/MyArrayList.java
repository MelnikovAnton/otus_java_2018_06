package ru.otus.HW3;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MyArrayList<T> implements List<T> {

    private T[] array;

    private T elm;

    private int size;
    private int arraySize;
    private final int DEAFAULT_SIZE =10;


    private void grow(){
        T[] oldArr=array;
        array= (T[]) new Object[arraySize+DEAFAULT_SIZE];
        System.arraycopy(oldArr,0,array,0,oldArr.length);
        arraySize=array.length;
    }

    public MyArrayList() {
        this.size=0;
        array = (T[]) new Object[DEAFAULT_SIZE];
        arraySize=DEAFAULT_SIZE;
    }

    public MyArrayList(T[] arr) {
        this.size=arr.length;
        array = arr;
        arraySize=arr.length;
    }

    //=================================================

    @Override
    public Iterator<T> iterator() {
        return new MyListIter(-1);
    }

    @Override
    public ListIterator<T> listIterator() {
        return new MyListIter(-1);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new MyListIter(index);
    }


    @Override
    public String toString() {
        String out = Arrays.stream(array)
                .limit(size)
                .map(Object::toString).
                        collect(Collectors.joining(", "));
        return "MyArrayList{[" + out + "]}";
    }

    //=================================================
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public boolean contains(Object o) {
        for (T item:array) {
            if (o.equals(item)) return true;
        };
        return false;
    }

   @Override
    public T[] toArray() {
        T[] result= (T[]) new Object[size];
        System.arraycopy(array,0,result,0,size);
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        System.arraycopy(array,0,a,0,size);
        return a;
    }

    @Override
    public boolean add(T t) {
        if (arraySize-size<=0)
            grow();
        array[size]=t;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (int i=0;i<size;i++) {
            if (array[i].equals(o)){
                array[i]=null;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        T[] isArray = (T[]) c.toArray();
        boolean contains =true;
        if (isArray.length>size) return false;
        for (T item:isArray){
            if (!contains) return false;
            contains = contains && contains(item);
        }
        return contains;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        T[] newArr = (T[])c.toArray();
        int len=newArr.length;
        if (arraySize>(size=newArr.length+size)){
            System.arraycopy(newArr,0,array,0,len);
            return true;
        } else {
            grow();
            addAll(c);
        }
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        T[] newArr = (T[])c.toArray();
        int len=newArr.length;
        if (arraySize>(newArr.length+index+size)){
            T[] right = (T[])new Object[size-index];
            System.arraycopy(array,index,right,0,size-index);
            System.arraycopy(newArr,0,array,index,len);
            System.arraycopy(right,0,array,index+len,right.length);
            size +=len;
            return true;
        } else {
            grow();
            addAll(index,c);
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (int i = 0;i<size;i++){
            if (c.contains(array[i])) array[i] = null;
        }
        return true;
    }

    //TODO
    @Override
    public boolean retainAll(Collection<?> c) {
  //      throw new UnsupportedOperationException();
        return false;
    }

    @Override
    public void clear() {
        this.size=0;
        this.array = (T[]) new Object[DEAFAULT_SIZE];
        arraySize=array.length;
    }

    @Override
    public T get(int index) {
        return array[index];
    }

    @Override
    public T set(int index, T element) {
        array[index] = element;
        return array[index];
    }

    @Override
    public void add(int index, T element) {
        T[] right = (T[])new Object[arraySize-index];
        System.arraycopy(array,index,right,0,size-index);
        array[index]=element;
        System.arraycopy(right,0,array,index+1,1);
        size++;
    }

    @Override
    public T remove(int index) {
        T result=array[index];
        array[index]=null;
        return result;
    }

    @Override
    public int indexOf(Object o) {
        for (int i=0;i<size;i++){
            if (o.equals(array[i])) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i=size;i>0;i--){
            if (o.equals(array[i])) return i;
        }
        return -1;
    }
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        T[] newArray=(T[]) new Object[toIndex-fromIndex+1];
        System.arraycopy(array,fromIndex,newArray,0,toIndex-fromIndex+1);
        System.out.println();
        return new MyArrayList<T>(newArray);
    }



    //========================= ITERATOR ===================================
    private class MyListIter implements ListIterator<T> {
        int cursor;

        public MyListIter(int cursor) {
            this.cursor = cursor;
        }

        @Override
        public boolean hasNext() {
            return cursor+1<=size;
        }

        @Override
        public T next() {
            cursor++;
            return array[cursor];
        }

        @Override
        public boolean hasPrevious() {
            return cursor>0;
        }

        @Override
        public T previous() {
            cursor--;
            return array[cursor];
        }

        @Override
        public int nextIndex() {
            return cursor+1;
        }

        @Override
        public int previousIndex() {
            return cursor-1;
        }

        @Override
        public void remove() {
            array[cursor]=null;
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            for (int i=cursor;i<size;i++){
                action.accept(array[i]);
            }
        }

        @Override
        public void set(T t) {
            array[cursor]=t;
        }

        @Override
        public void add(T t) {
            MyArrayList.this.add(cursor,t);
            cursor++;
        }
    }

}
