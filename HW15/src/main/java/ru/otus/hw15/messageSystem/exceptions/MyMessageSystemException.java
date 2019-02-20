package ru.otus.hw15.messageSystem.exceptions;

public class MyMessageSystemException extends Exception {
    public MyMessageSystemException(String m,Exception e) {
        super(m,e);
    }

    public MyMessageSystemException(String m) {
        super(m);
    }
}
