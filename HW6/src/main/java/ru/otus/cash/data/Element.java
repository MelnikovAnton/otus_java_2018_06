package ru.otus.cash.data;

import java.io.Serializable;

public class Element implements Serializable {


    final byte[]  data =new byte[1024];
    final String description;


    public Element(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Element{" +
                "description='" + description + '\'' +
                '}';
    }
}
