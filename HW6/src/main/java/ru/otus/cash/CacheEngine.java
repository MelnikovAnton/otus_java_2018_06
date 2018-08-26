package ru.otus.cash;

public interface CacheEngine<K,V> {

        void put(MyElement<K, V> element);

        void put(K key,V value);

        MyElement<K, V> getElement(K key);

        V get(K key);

        int getHitCount();

        int getMissCount();

        void dispose();

        int getSize();

}
