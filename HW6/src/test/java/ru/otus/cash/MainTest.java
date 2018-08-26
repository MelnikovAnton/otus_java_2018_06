package ru.otus.cash;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cash.data.DiskData;
import ru.otus.cash.data.Element;
import ru.otus.cash.data.GetElementsWithCache;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

/**
 *   VM options: -Xmx256m -Xms256m
 *
 */
public class MainTest {

    private static Logger logger = LoggerFactory.getLogger(MainTest.class);

    @BeforeClass
    public static void createFile(){
        DiskData diskData=new DiskData();
        for (int i = 0;i <1000;i++){
            diskData.put(Integer.toString(i),new Element("Element " + i));
        }

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream("diskData.ser"))) {
            oos.writeObject(diskData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testSoftLinkCache(){
        int size = 1000;
        CacheEngine<Integer, LageElement> cache = new CacheEngineImpl<>(size, 0, 0, true);

        for (int i=0;i<size;i++){
            cache.put(i,new LageElement());
        }

        System.gc();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i=0;i<size;i++){
            LageElement elm = cache.get(i);
            String value = (elm == null) ? "null": "LageObject";

//            logger.info(i+ " " + value );
        }

        logger.info("Cache hit " +cache.getHitCount());
        logger.info("Cache miss " +cache.getMissCount());
        cache.dispose();

    }


    @Test
    public void eternalCache(){
        long testStart=System.currentTimeMillis();
        int size = 200;
        CacheEngine<String, Element> cache = new CacheEngineImpl<>(size, 0, 0, true);
        GetElementsWithCache action = new GetElementsWithCache(cache);
        for (int i=0; i<10000;i++){
 //           long start=System.currentTimeMillis();
            int randomNum = ThreadLocalRandom.current().nextInt(0, 1001);
            action.getElement(Integer.toString(randomNum));
            //long stop=System.currentTimeMillis() - start;
//            logger.info(randomNum + " getting " + i +" element take " + stop);
//            logger.info("Cache size: " + cache.getSize());
//            logger.info("Cache hits: " + cache.getHitCount());
//            logger.info("Cache misses: " + cache.getMissCount());
        }

        logger.info("Cache hits: " + cache.getHitCount());
        logger.info("Cache misses: " + cache.getMissCount());

        long testTime = System.currentTimeMillis()-testStart;
        logger.info("It takes " + testTime);

        logger.info("from cache: " + action.getFromCache());
        logger.info("from disk: " + action.getFromDisk());
    }

    @Test
    public void lifeCache(){
        long testStart=System.currentTimeMillis();
        int size = 200;
        CacheEngine<String, Element> cache = new CacheEngineImpl<>(size, 1000, 0, false);
        GetElementsWithCache action = new GetElementsWithCache(cache);
        for (int i=0; i<10000;i++){
  //          long start=System.currentTimeMillis();
            int randomNum = ThreadLocalRandom.current().nextInt(0, 1001);
            action.getElement(Integer.toString(randomNum));
//            long stop=System.currentTimeMillis() - start;
//            logger.info(randomNum + " getting " + i +" element take " + stop);
//            logger.info("Cache size: " + cache.getSize());
//            logger.info("Cache hits: " + cache.getHitCount());
//            logger.info("Cache misses: " + cache.getMissCount());
        }

        logger.info("Cache hits: " + cache.getHitCount());
        logger.info("Cache misses: " + cache.getMissCount());

        long testTime = System.currentTimeMillis()-testStart;
        logger.info("It takes " + testTime);

        logger.info("from cache: " + action.getFromCache());
        logger.info("from disk: " + action.getFromDisk());


    }

    @Test
    public void idleCache(){
        long testStart=System.currentTimeMillis();
        int size = 200;
        CacheEngine<String, Element> cache = new CacheEngineImpl<>(size, 0, 30000, false);
        GetElementsWithCache action = new GetElementsWithCache(cache);
        for (int i=0; i<10000;i++){
  //          long start=System.currentTimeMillis();
            int randomNum = ThreadLocalRandom.current().nextInt(0, 1001);
            action.getElement(Integer.toString(randomNum));
//            long stop=System.currentTimeMillis() - start;
//            logger.info(randomNum + " getting " + i +" element take " + stop);
//            logger.info("Cache size: " + cache.getSize());
//            logger.info("Cache hits: " + cache.getHitCount());
//            logger.info("Cache misses: " + cache.getMissCount());
        }

        logger.info("Cache hits: " + cache.getHitCount());
        logger.info("Cache misses: " + cache.getMissCount());

        long testTime = System.currentTimeMillis()-testStart;
        logger.info("It takes " + testTime);

        logger.info("from cache: " + action.getFromCache());
        logger.info("from disk: " + action.getFromDisk());
    }

    @Test
    public void minHitCache(){
        long testStart=System.currentTimeMillis();
        int size = 200;
        CacheEngine<String, Element> cache = new CacheEngineImpl<>(size, 1, 0, false);
        GetElementsWithCache action = new GetElementsWithCache(cache);
        for (int i=0; i<10000;i++){
            //          long start=System.currentTimeMillis();
            int randomNum = ThreadLocalRandom.current().nextInt(0, 1001);
            action.getElement(Integer.toString(randomNum));
//            long stop=System.currentTimeMillis() - start;
//            logger.info(randomNum + " getting " + i +" element take " + stop);
//            logger.info("Cache size: " + cache.getSize());
//            logger.info("Cache hits: " + cache.getHitCount());
//            logger.info("Cache misses: " + cache.getMissCount());
        }

        logger.info("Cache hits: " + cache.getHitCount());
        logger.info("Cache misses: " + cache.getMissCount());

        long testTime = System.currentTimeMillis()-testStart;
        logger.info("It takes " + testTime);

        logger.info("from cache: " + action.getFromCache());
        logger.info("from disk: " + action.getFromDisk());


    }


    @Test
    public void noCache(){
        long testStart=System.currentTimeMillis();
        int size = 200;
        CacheEngine<String, Element> cache = new CacheEngineImpl<>(size, 1, 0, false);
        GetElementsWithCache action = new GetElementsWithCache(cache);
        for (int i=0; i<10000;i++){
            //          long start=System.currentTimeMillis();
            int randomNum = ThreadLocalRandom.current().nextInt(0, 1001);
            action.getFromDisk(Integer.toString(randomNum));
        }

        logger.info("Cache hits: " + cache.getHitCount());
        logger.info("Cache misses: " + cache.getMissCount());

        long testTime = System.currentTimeMillis()-testStart;
        logger.info("It takes " + testTime);

        logger.info("from cache: " + action.getFromCache());
        logger.info("from disk: " + action.getFromDisk());


    }

    class LageElement{
        private byte[] bytes = new byte[1024*1024];
    }
}