package ru.otus.myORM.service;

import ru.otus.myORM.exceptions.MyDBException;
import ru.otus.myORM.models.DataSet;
import ru.otus.myORM.models.UserDataSet;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface DBService<T extends DataSet> extends AutoCloseable {
    String getMetaData();


    <T extends DataSet> T getById(long id) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException;

    List<T> getAll() throws SQLException, Exception;

    void deleteTables() throws SQLException;


    void prepareTable() throws SQLException, MyDBException;

    <T extends DataSet> void save(T user) throws SQLException;

    void truncateTable() throws SQLException;

}
