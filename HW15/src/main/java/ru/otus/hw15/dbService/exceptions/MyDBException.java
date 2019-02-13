package ru.otus.hw15.dbService.exceptions;

public class MyDBException extends Exception {

    public MyDBException(String m,Exception e) {
        super(m,e);
    }

    public MyDBException(String m) {
        super(m);
    }
}
