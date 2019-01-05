package MyORM2.helpers;

import MyORM2.exceptions.MyDBException;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@FunctionalInterface
public interface ResultHandler<T> {
    List<T> handle(ResultSet result) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException, MyDBException;
}