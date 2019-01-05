package MyORM2.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {
  //  private static final String URL = "jdbc:postgresql://127.0.0.1:5432/test";
  private static final String URL = "jdbc:h2:mem:";
    private static Connection connection;
    public static Connection getConnection() {
        try {
            if (connection!=null) return connection;
            connection= DriverManager.getConnection(URL,"sa","");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
