package ru.otus.gc.data;

import java.util.Objects;
import java.util.Random;

public class BadKey extends Parent {
private static final Random random= new Random();


    public BadKey(String key){

        this.key=key;
    }

    @Override
    public boolean equals(Object o) {
       return false;
    }

    @Override
    public int hashCode() {
        return random.nextInt();
    }
}
