package ru.otus.myORM.exceptions;

import ru.otus.myORM.Executor;

public class MyDBException extends Exception {

    public MyDBException(String m) {
        super(m);
    }
}
