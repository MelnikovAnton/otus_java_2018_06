package ru.otus.myORM.helpers;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultHandler<T> {
    void handle(ResultSet result) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException;
}