package ru.otus.cash.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cash.CacheEngine;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class ElementService {
    private static Logger logger = LoggerFactory.getLogger(ElementService.class);

    private int fromCache = 0;
    private  int fromDisk = 0;

    private final CacheEngine<String,Element> cache;

    public ElementService(CacheEngine cache) {
        this.cache=cache;
    }

    public  Element getElement(String key){
        Element elm=cache.get(key);
        if (elm !=null) {
            fromCache++;
            logger.debug("Element in cache " + key);
            return elm;
        }
        fromDisk++;
        logger.debug("Element not in cache " + key);
        Element element = getFromDisk(key);
        cache.put(key,element);
        return element;
    }

    public Element getFromDisk(String key){
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream("diskData.ser"))) {
            DiskData dataObj= (DiskData) ois.readObject();
            return dataObj.get(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Element("null");
        }
    }

    public int getFromCache() {
        return fromCache;
    }

    public int getFromDisk() {
        return fromDisk;
    }
}
