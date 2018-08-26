package ru.otus.cash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *   VM options: -Xmx256m -Xms256m
 *
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("Eternal Cache example");
        new Main().eternalCacheExample();
        logger.info("Life Cache example");
        new Main().lifeCacheExample();
        logger.info("Idle Cache example");
        new Main().idleCacheExample();

    }



    private void eternalCacheExample() {
        int size = 6;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(size, 0, 0, true);

        for (int i = 0; i < 10; i++) {
            cache.put(new MyElement<>(i, "String: " + i));
        }

        for (int i = 0; i < 10; i++) {
            String element = cache.get(i);
            logger.info("String for " + i + ": " + (element != null ? element : "null"));
        }

        logger.info("Cache hits: " + cache.getHitCount());
        logger.info("Cache misses: " + cache.getMissCount());

        cache.dispose();
    }

    private void lifeCacheExample()  {
        int size = 5;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(size, 1000, 0, false);

        for (int i = 0; i < size; i++) {
            cache.put(new MyElement<>(i, "String: " + i));
        }

        for (int i = 0; i < size; i++) {
            String element = cache.get(i);
            logger.info("String for " + i + ": " + (element != null ? element : "null"));
        }

        logger.info("Cache hits: " + cache.getHitCount());
        logger.info("Cache misses: " + cache.getMissCount());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < size; i++) {
            String element = cache.get(i);
            logger.info("String for " + i + ": " + (element != null ? element : "null"));
        }

        logger.info("Cache hits: " + cache.getHitCount());
        logger.info("Cache misses: " + cache.getMissCount());

        cache.dispose();
    }

    private void idleCacheExample()  {
        int size = 10;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(size, 0, 1000, false);

        for (int i = 0; i < size; i++) {
            cache.put(new MyElement<>(i, "String: " + i));
        }
        for (int i = 0; i < size; i++) {
            String element = cache.get(i);
            logger.info("String for " + i + ": " + (element != null ? element : "null"));
        }
        logger.info("Cache hits: " + cache.getHitCount());
        logger.info("Cache misses: " + cache.getMissCount());


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < size; i+=2) {
            String element = cache.get(i);
            logger.info("String for " + i + ": " + (element != null ? element : "null"));
        }
        logger.info("Cache hits: " + cache.getHitCount());
        logger.info("Cache misses: " + cache.getMissCount());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < size; i++) {
            String element = cache.get(i);
            logger.info("String for " + i + ": " + (element != null ? element : "null"));
        }

        logger.info("Cache hits: " + cache.getHitCount());
        logger.info("Cache misses: " + cache.getMissCount());

        cache.dispose();



    }
    class LageElement{
        private byte[] bytes = new byte[1024*1024];
    }
}
