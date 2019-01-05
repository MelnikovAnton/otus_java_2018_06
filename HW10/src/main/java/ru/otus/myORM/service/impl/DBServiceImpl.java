package ru.otus.myORM.service.impl;

import ru.otus.myORM.Executor;
import ru.otus.myORM.exceptions.MyDBException;
import ru.otus.myORM.helpers.ConnectionHelper;
import ru.otus.myORM.helpers.DBHelper;
import ru.otus.myORM.helpers.QueryBuilder;
import ru.otus.myORM.models.DataSet;
import ru.otus.myORM.service.DBService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBServiceImpl<T> implements DBService {
    private final Connection connection;
    //   private static final String CREATE_TABLE = "create table if not exists ? (id bigint auto_increment, ? , primary key (id))";


    private Class type;

    private final Mapper mapper;

    Class<T> getType() {
        return type;
    }

    public <T extends DataSet> DBServiceImpl(Class<T> clazz) throws MyDBException {
        connection = (Connection) ConnectionHelper.getConnection();
        this.type = clazz;
        mapper = new Mapper();


    }

    @Override
    public void truncateTable() throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DBHelper.getTruncateTable(type));
        ps.execute();
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

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAll() throws Exception {


        Executor executor = new Executor(getConnection());
        List<T> rez = new ArrayList<>();

        executor.execQuery(QueryBuilder.getSelect(type), rs -> {
            while (rs.next()) {
                T dataSet = null;
                dataSet = mapper.getInstanse(rs);
                rez.add(dataSet);

            }
        });


        return rez;
    }


    @Override
    public void prepareTable() {
        Executor exec = new Executor(getConnection());
        try {
            exec.createTable(type);
        } catch (SQLException | MyDBException e) {
            e.printStackTrace();
        }
        System.out.println("Table created");
    }

    @Override
    public void save(DataSet dataSet) throws SQLException {
        Executor exec = new Executor(getConnection());
        exec.execUpdate(DBHelper.getInsertSQL((DataSet) dataSet), PreparedStatement::executeUpdate);
    }

    @Override
    public DataSet getById(long id) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Executor executor = new Executor(getConnection());
        final DataSet[] dataSet = {null};
        executor.execQuery(DBHelper.getSelectQuery(type), rs->{

            if (rs.next()) dataSet[0] = (DataSet) mapper.getInstanse(rs);
        });
        return dataSet[0];
    }


    @Override
    public void deleteTables() throws SQLException {
        Executor exec = new Executor(getConnection());
        exec.execUpdate(DBHelper.getDropTable(type), PreparedStatement::executeUpdate);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    protected Connection getConnection() {
        return connection;
    }

    private class Mapper {

        private final Constructor<? extends DataSet> constr;

        private final Method[] methods;

        private Mapper() throws MyDBException {
            this.constr = getDataSetConstructor(type);
            this.methods = type.getMethods();

        }

        private T getInstanse(ResultSet rs) {
            T dataSet = null;
            try {
                dataSet = (T) constr.newInstance();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    Method m = getSetter(columnName);
                    invokeMethod(dataSet, m, rs, columnName);
                }
            } catch (InstantiationException | MyDBException | InvocationTargetException | SQLException | IllegalAccessException e) {
                e.printStackTrace();
            }


            return dataSet;
        }

        private void invokeMethod(Object o, Method method, ResultSet rs, String columnName) throws SQLException, InvocationTargetException, IllegalAccessException {
            String type = method.getParameterTypes()[0].getSimpleName();
            System.out.println(type);
            if ("int".equals(type) || "Integer".equals(type)) {
                method.invoke(o, rs.getInt(columnName));
            } else if ("String".equals(type)) {
                method.invoke(o, rs.getString(columnName));
            } else if ("float".equals(type) || "Float".equals(type)) {
                method.invoke(o, rs.getFloat(columnName));
            } else if ("double".equals(type) || "Double".equals(type)) {
                method.invoke(o, rs.getDouble(columnName));
            } else if ("boolean".equals(type) || "Boolean".equals(type)) {
                method.invoke(o, rs.getBoolean(columnName));
            } else if ("byte".equals(type) || "Byte".equals(type)) {
                method.invoke(o, rs.getByte(columnName));
            } else if ("short".equals(type) || "Short".equals(type)) {
                method.invoke(o, rs.getShort(columnName));
            } else if ("long".equals(type) || "Long".equals(type)) {
                method.invoke(o, rs.getLong(columnName));
            }

        }

        private <T extends DataSet> Constructor<T> getDataSetConstructor(Class<?> clazz) throws MyDBException {
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor con : constructors) {
                if (con.getParameterCount() == 0) return con;
            }
            throw new MyDBException("DataSet has no default constructor.");
        }

        private Method getSetter(String name) throws MyDBException {
            return Arrays.stream(methods)
                    .filter(m -> m.getName().toLowerCase().contains(name))
                    .filter(m -> m.getName().contains("set"))
                    .findFirst().orElseThrow(() -> new MyDBException("Setter not found"));

        }
    }
}
