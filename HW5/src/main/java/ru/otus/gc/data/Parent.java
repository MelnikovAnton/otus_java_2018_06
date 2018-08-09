package ru.otus.gc.data;

import java.util.Objects;

public class Parent {

    String key;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parent parent = (Parent) o;
        return Objects.equals(key, parent.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
