package ru.otus.cash;

import java.lang.ref.SoftReference;

import static java.lang.System.*;

public class MyElement<K, V>  {
        private final K key;
        private final SoftReference<V> value;
 //   private final V value;
        private final long creationTime;
        private long lastAccessTime;

    public MyElement(K key, V value) {
        this.key = key;
        this.value = new SoftReference<V>(value);
//        this.value = value;
        this.creationTime = currentTimeMillis();
    }


        public K getKey() {
            return key;
        }

        public V getValue() {
            return value.get();
        }

//    public V getValue() {
//            return value;
//        }

        public long getCreationTime() {
            return creationTime;
        }

        public long getLastAccessTime() {
            return lastAccessTime;
        }

        public void setAccessed() {
            lastAccessTime = currentTimeMillis();
        }


}
