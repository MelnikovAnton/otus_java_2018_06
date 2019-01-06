package ru.otus.hibernate.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hibernate.myORM.Executor;
import ru.otus.hibernate.exceptions.MyDBException;
import ru.otus.hibernate.myORM.helpers.ConnectionHelper;
import ru.otus.hibernate.myORM.helpers.DBHelper;
import ru.otus.hibernate.myORM.models.DataSet;

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

@SuppressWarnings("unchecked")
public class DBServiceImpl implements DBService {
    private final Connection connection = ConnectionHelper.getConnection();
    private static Logger logger = LoggerFactory.getLogger(DBServiceImpl.class);

    public DBServiceImpl() throws MyDBException {
    }

    @Override
    public <T extends DataSet> void save(T dataset) throws MyDBException {
        String sql = DBHelper.getInsertSQL(dataset.getClass());
        Executor executor = new Executor(getConnection());
        Object[] values = DBHelper.getFieldValues(dataset);
        int key = executor.execUpdate(sql, values);
        dataset.setId(key);

        logger.info("Object seved to DB \n" + dataset);
    }

    @Override
    public <T extends DataSet> List<T> load(long id, Class<T> clazz) throws MyDBException {
        String sql = DBHelper.getSelectQuery(clazz);
        sql = sql.substring(0, sql.length() - 1).concat(" where id = ? ;");
        Executor executor = new Executor(getConnection());
        Mapper mapper = new Mapper(clazz);
        List<DataSet> result = executor.execQuery(sql, id, rs -> {
            List<DataSet> rez = new ArrayList<>();
            while (rs.next()) {

                DataSet instance = mapper.getInstanse(rs);
                rez.add(instance);
            }
            return rez;
        });
        if (result.size() == 0) throw new MyDBException("DataSet with id " + id + " not found");

        return (List<T>) result;
    }

    @Override
    public <T extends DataSet> List<T> loadByName(String name, Class<T> clazz) throws MyDBException {
        String sql = DBHelper.getSelectQuery(clazz);
        sql = sql.substring(0, sql.length() - 1).concat(" where name = ? ;");
        Executor executor = new Executor(getConnection());
        Mapper mapper = new Mapper(clazz);
        List<DataSet> result = executor.execQuery(sql, name, rs -> {
            List<DataSet> rez = new ArrayList<>();
            while (rs.next()) {

                DataSet instance = mapper.getInstanse(rs);
                rez.add(instance);
            }
            return rez;
        });
        if (result.size() == 0) throw new MyDBException("DataSet with name " + name + " not found");

        return (List<T>) result;
    }


    @Override
    public <T extends DataSet> List<T> loadAll(Class<T> clazz) throws MyDBException {
        String sql = DBHelper.getSelectQuery(clazz);
        Executor executor = new Executor(getConnection());
        Mapper mapper = new Mapper(clazz);
        List<DataSet> result = executor.execQuery(sql, rs -> {
            List<DataSet> rez = new ArrayList<>();
            while (rs.next()) {

                DataSet instance = mapper.getInstanse(rs);
                rez.add(instance);
            }
            return rez;
        });
        if (result.size() == 0) throw new MyDBException("No Data for " + clazz.getName());

        return (List<T>) result;
    }


    @Override
    public void truncateTable(Class clazz) throws MyDBException {
        try {
            PreparedStatement ps = connection.prepareStatement(DBHelper.getTruncateTable(clazz));
            ps.execute();

        } catch (SQLException e) {
            throw new MyDBException("Cannot truncate table",e);
        }
        logger.info("table cleared");
    }

    @Override
    public void dropTable(Class clazz) throws MyDBException {
        try {
            PreparedStatement ps = connection.prepareStatement(DBHelper.getDropTable(clazz));
            ps.execute();
        } catch (SQLException e) {
            throw new MyDBException("Cannot drop table",e);
        }

        logger.info("table dropped");
    }

    @Override
    public void createTable(Class clazz) throws MyDBException {
        String CREATE_TABLE = DBHelper.getCreateTableSQL(clazz);
        logger.info(CREATE_TABLE);
        try {
            PreparedStatement ps = connection.prepareStatement(CREATE_TABLE);
            ps.execute();
        } catch (SQLException e) {
            throw new MyDBException("Cannot drop table",e);
        }
        logger.info("table created");

    }

    @Override
    public String getMetadata() throws MyDBException {
        try {
            return "Connected to: " + getConnection().getMetaData().getURL() + "\n" +
                    "DB name: " + getConnection().getMetaData().getDatabaseProductName() + "\n" +
                    "DB version: " + getConnection().getMetaData().getDatabaseProductVersion() + "\n" +
                    "Driver: " + getConnection().getMetaData().getDriverName() + "\n" +
                    "Schema: " + getConnection().getSchema();
        } catch (SQLException e) {
            throw new MyDBException("Cannot get metadata",e);
        }
    }


    @Override
    public void close() throws Exception {
        connection.close();
    }

    private Connection getConnection() {
        return connection;
    }

    @SuppressWarnings("TypeParameterHidesVisibleType")
    private class Mapper<T extends DataSet> {

        private final Constructor<? extends DataSet> constr;

        private final Method[] methods;

        private Mapper(Class type) throws MyDBException {
            this.constr = getDataSetConstructor(type);
            this.methods = type.getMethods();

        }

        private T getInstanse(ResultSet rs) throws MyDBException {
            T dataSet = null;
            try {
                dataSet = (T) constr.newInstance();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    Method m = getSetter(columnName);
                    invokeMethod(dataSet, m, rs, columnName);
                }
            } catch (InstantiationException | InvocationTargetException | SQLException | IllegalAccessException | MyDBException e) {
                throw new MyDBException("Cannot get instance",e);
            }


            return dataSet;
        }

        private void invokeMethod(Object o, Method method, ResultSet rs, String columnName) throws MyDBException {
            String type = method.getParameterTypes()[0].getSimpleName();
            try {
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
            } catch (Exception e) {
                throw new MyDBException("Type not supported",e);
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
                    .filter(m -> m.getName().toLowerCase().contains(name.toLowerCase()))
                    .filter(m -> m.getName().contains("set"))
                    .findFirst().orElseThrow(() -> new MyDBException("Setter not found"));

        }
    }
}
