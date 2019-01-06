package ru.otus.hibernate.service;

import ru.otus.hibernate.exceptions.MyDBException;
import ru.otus.hibernate.myORM.models.DataSet;

import java.util.List;

public class DBServiceHibernateImpl implements DBService {
    @Override
    public <T extends DataSet> void save(T user) throws MyDBException {

    }

    @Override
    public <T extends DataSet> List<T> load(long id, Class<T> clazz) throws MyDBException {
        return null;
    }

    @Override
    public <T extends DataSet> List<T> loadByName(String name, Class<T> clazz) throws MyDBException {
        return null;
    }

    @Override
    public <T extends DataSet> List<T> loadAll(Class<T> clazz) throws MyDBException {
        return null;
    }

    @Override
    public void truncateTable(Class clazz) throws MyDBException {

    }

    @Override
    public void dropTable(Class clazz) throws MyDBException {

    }

    @Override
    public void createTable(Class clazz) throws MyDBException {

    }

    @Override
    public String getMetadata() throws MyDBException {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
