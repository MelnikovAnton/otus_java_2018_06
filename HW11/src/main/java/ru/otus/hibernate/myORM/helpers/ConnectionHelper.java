package ru.otus.hibernate.myORM.helpers;

import ru.otus.hibernate.exceptions.MyDBException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {
  //  private static final String URL = "jdbc:postgresql://127.0.0.1:5432/test";
  private static final String URL = "jdbc:h2:mem:";
    private static Connection connection;
    public static Connection getConnection() throws MyDBException {
        try {
            if (connection!=null) return connection;
            connection= DriverManager.getConnection(URL,"sa","");
            return connection;
        } catch (SQLException e) {
            throw new MyDBException("Cannot get connection",e);
        }
    }
}
