package MyORM2;

import MyORM2.exceptions.MyDBException;
import MyORM2.helpers.ResultHandler;


import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;

public class Executor {
    private final Connection connection;

    public Executor(Connection connection){
        this.connection=connection;
    }

    public int execUpdate(String sql, Object ... values) throws MyDBException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
            preparedStatement.executeUpdate();
            ResultSet result = preparedStatement.getGeneratedKeys();
            result.next();
            return result.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MyDBException("Cannot execute update");
        }
    }

    public<T> List<T> execQuery(String query, ResultHandler<T> handler) throws MyDBException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.executeQuery();
            return handler.handle(stmt.getResultSet());
        } catch (IllegalAccessException | InstantiationException | SQLException | InvocationTargetException e) {
            e.printStackTrace();
            throw new MyDBException("cannot exec Query");
        }

    }


}
