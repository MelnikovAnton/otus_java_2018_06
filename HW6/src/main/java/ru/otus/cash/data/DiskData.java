package ru.otus.cash.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DiskData implements Serializable {

    private final Map<String,Element> map= new HashMap<>();

    public void put(String key,Element element){
        map.put(key,element);
    }

    public Element get(String key){
        return map.getOrDefault(key,new Element("null"));
    }

}
