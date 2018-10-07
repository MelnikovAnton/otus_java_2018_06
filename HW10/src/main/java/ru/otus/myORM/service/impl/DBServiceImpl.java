package ru.otus.myORM.service.impl;

import ru.otus.myORM.Executor;
import ru.otus.myORM.exceptions.MyDBException;
import ru.otus.myORM.helpers.ConnectionHelper;
import ru.otus.myORM.helpers.DBHelper;
import ru.otus.myORM.models.DataSet;
import ru.otus.myORM.service.DBService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DBServiceImpl<T> implements DBService {
    private final Connection connection;
 //   private static final String CREATE_TABLE = "create table if not exists ? (id bigint auto_increment, ? , primary key (id))";


    private Class type;

    Class<T> getType(){return type;}

    public <T extends DataSet> DBServiceImpl(Class<T> clazz) {
        connection = (Connection) ConnectionHelper.getConnection();
        this.type=clazz;

    }

    @Override
    public String getMetaData() {
        try {
            return "Connected to: " + getConnection().getMetaData().getURL() + "\n" +
                    "DB name: " + getConnection().getMetaData().getDatabaseProductName() + "\n" +
                    "DB version: " + getConnection().getMetaData().getDatabaseProductVersion() + "\n" +
                    "Driver: " + getConnection().getMetaData().getDriverName() + "\n" +
                    "Schema: " + getConnection().getSchema();
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public void add(List dataSets) throws SQLException {
        Executor exec = new Executor(getConnection());
        for (Object dataSet:dataSets) {
            exec.execUpdate(DBHelper.getInsertSQL((DataSet) dataSet), statement -> {
                statement.executeUpdate();
            });
        }
    }


    @Override
    public  void prepareTable()  {
        Executor exec = new Executor(getConnection());
        try {
            exec.createTable(type);
        } catch (SQLException | MyDBException e) {
            e.printStackTrace();
        }
        System.out.println("Table created");
    }

    @Override
    public DataSet getById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<DataSet> getAll() throws SQLException {
        return null;
    }

    @Override
    public void deleteTables() throws SQLException {

    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    protected Connection getConnection() {
        return connection;
    }
}
