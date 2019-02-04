package ru.otus.hw15.service;


import ru.otus.hw15.exceptions.MyDBException;
import ru.otus.hw15.models.DataSet;

import java.util.List;

public interface DBService extends AutoCloseable {
    <T extends DataSet> void save(T user) throws MyDBException;
    <T extends DataSet> T load(long id, Class<T> clazz) throws MyDBException;
    <T extends DataSet> List<T> loadByName(String name, Class<T> clazz) throws MyDBException;
    <T extends DataSet> List<T> loadAll(Class<T> clazz) throws MyDBException;
    void truncateTable(Class clazz) throws MyDBException;
    void dropTable(Class clazz) throws MyDBException;
    void createTable(Class clazz) throws MyDBException;
    String getMetadata() throws MyDBException;
    <T extends DataSet> long getCount(Class<T> clazz) throws MyDBException;
}
