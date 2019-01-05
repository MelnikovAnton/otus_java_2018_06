package ru.otus.myORM;

import ru.otus.myORM.exceptions.MyDBException;
import ru.otus.myORM.helpers.DBHelper;
import ru.otus.myORM.helpers.ExecuteHandler;
import ru.otus.myORM.helpers.ResultHandler;
import ru.otus.myORM.models.DataSet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class Executor {
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }


    public<T> void execQuery(String query, ResultHandler<T> handler) throws SQLException, IllegalAccessException, InstantiationException, InvocationTargetException {
        try (PreparedStatement stmt = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
            System.out.println(query);
    if (stmt.getParameterMetaData().getParameterCount()==1) stmt.setObject(1,4);
            stmt.executeQuery();
            handler.handle(stmt.getResultSet());
        }
    }

    public void execUpdate(String update, ExecuteHandler handler) throws SQLException {
        try {
            PreparedStatement stmt = getConnection().prepareStatement(update,Statement.RETURN_GENERATED_KEYS);
            handler.accept(stmt);

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(Class clazz) throws SQLException, MyDBException {
        String CREATE_TABLE = DBHelper.getCreateTableSQL(clazz);
        System.out.println(CREATE_TABLE);
        PreparedStatement ps = connection.prepareStatement(CREATE_TABLE);
        ps.executeUpdate();
    }

    Connection getConnection() {
        return connection;
    }
}
