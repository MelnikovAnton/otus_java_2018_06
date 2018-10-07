package ru.otus.myORM.service;

import ru.otus.myORM.exceptions.MyDBException;
import ru.otus.myORM.models.DataSet;
import ru.otus.myORM.models.UserDataSet;

import java.sql.SQLException;
import java.util.List;

public interface DBService<T extends DataSet> extends AutoCloseable {
    String getMetaData();

   // void prepareTables() throws SQLException;

  //   void add(List<T> dataSets) throws SQLException;

    <T> T getById(int id) throws SQLException;

    List<T> getAll() throws SQLException;

    void deleteTables() throws SQLException;

    void add(List<DataSet> dataSets) throws SQLException;

    void prepareTable() throws SQLException, MyDBException;
}
