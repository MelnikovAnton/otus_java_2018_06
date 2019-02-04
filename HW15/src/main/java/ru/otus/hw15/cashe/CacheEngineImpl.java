package ru.otus.hw15.cashe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {
    private static Logger logger = LoggerFactory.getLogger(CacheEngineImpl.class);

    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;

    private final Map<K, MyElement<K, V>> elements = new LinkedHashMap<>();

    private final int EVICTION_TIMER = 5;

    private int hit = 0;
    private int miss = 0;

    private final Timer timer = new Timer();


    public CacheEngineImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
    }

    @Override
    public void put(MyElement<K, V> element) {
        logger.debug("put element " + element.getKey());
        if (elements.size() == maxElements) {
            K firstKey = elements.keySet().iterator().next();
            logger.debug("Rmuving cache element " + firstKey);
            elements.remove(firstKey);
        }

        K key = element.getKey();
        elements.put(key, element);

        if (!isEternal) {
            if (lifeTimeMs != 0) {
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }
            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
                timer.schedule(idleTimerTask, idleTimeMs);
            }
        }
    }

    @Override
    public void put(K key, V value) {
        put(new MyElement<K,V>(key,value));
    }

    @Override
    public MyElement<K, V> getElement(K key) {
        MyElement<K, V> element = elements.get(key);
        if (element != null && element.getValue() != null) {
            hit++;
            element.setAccessed();
            logger.debug("Cache hit " + hit);
        } else {
            miss++;
            logger.debug("Cache miss " + miss);
        }
        return element;
    }

    @Override
    public V get(K key) {
        MyElement<K, V> elm = getElement(key);
        return (elm!=null)?elm.getValue():null;
    }

    @Override
    public int getHitCount() {
        return hit;
    }

    @Override
    public int getMissCount() {
        return miss;
    }

    @Override
    public void dispose() {
        logger.debug("Cache dispose " + hit);
        elements.clear();
        timer.cancel();
    }

    @Override
    public int getSize() {
        return elements.size();
    }

    private TimerTask getTimerTask(final K key, Function<MyElement<K, V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                MyElement<K, V> element = elements.get(key);
                if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                    elements.remove(key);
                    logger.debug("Cache remove element by timer " + key);
                    this.cancel();
                }
            }
        };
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + EVICTION_TIMER;
    }
}
