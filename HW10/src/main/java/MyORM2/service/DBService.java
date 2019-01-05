package MyORM2.service;

import MyORM2.exceptions.MyDBException;
import MyORM2.models.DataSet;

public interface DBService extends AutoCloseable {
    <T extends DataSet> void save(T user) throws MyDBException;
    <T extends DataSet> T load(long id, Class<T> clazz) throws MyDBException;
    void truncateTable(Class clazz) throws MyDBException;
    void dropTable(Class clazz) throws MyDBException;
    void createTable(Class clazz) throws MyDBException;
    String getMetadata() throws MyDBException;
}
