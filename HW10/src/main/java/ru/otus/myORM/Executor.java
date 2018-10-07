package ru.otus.myORM;

import ru.otus.myORM.exceptions.MyDBException;
import ru.otus.myORM.helpers.DBHelper;
import ru.otus.myORM.helpers.ExecuteHandler;
import ru.otus.myORM.helpers.ResultHandler;
import ru.otus.myORM.models.DataSet;

import java.lang.reflect.Field;
import java.sql.*;

public class Executor<T extends DataSet> {
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }


    public<T extends DataSet> void execQuery(String query, ResultHandler<T> handler) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            ResultSet result = stmt.getResultSet();
            handler.handle(result);
        }
    }

    public<T extends DataSet> int execUpdate(String update, ExecuteHandler handler) throws SQLException {
        try {
            PreparedStatement stmt = getConnection().prepareStatement(update);
            handler.accept(stmt);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void createTable(Class clazz) throws SQLException, MyDBException {
        Field[] fields = clazz.getDeclaredFields();
        String CREATE_TABLE = DBHelper.getCreateTableSQL(clazz);
        PreparedStatement ps = connection.prepareStatement(CREATE_TABLE);
        ps.executeUpdate();
    }

    public void save(T dataSet){

    }

    public T load(long id){
        return (T) new Object();
    }

    Connection getConnection() {
        return connection;
    }
}
