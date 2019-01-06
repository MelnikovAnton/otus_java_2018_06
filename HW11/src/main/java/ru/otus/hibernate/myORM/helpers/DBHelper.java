package ru.otus.hibernate.myORM.helpers;



import ru.otus.hibernate.exceptions.MyDBException;
import ru.otus.hibernate.models.DataSet;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DBHelper {
    public static String getTruncateTable(Class<? extends DataSet> clazz) {
        String sql = "truncate table %s";
        return String.format(sql, clazz.getSimpleName());
    }

    public static String getDropTable(Class<? extends DataSet> clazz) {
        String sql = "drop table %s";
        return String.format(sql, clazz.getSimpleName());
    }

    public static String getCreateTableSQL(Class<? extends DataSet> clazz) throws MyDBException {
        String sql = "create table if not exists %s (id  SERIAL PRIMARY KEY, %s)";
        String table_name = clazz.getSimpleName().toLowerCase();
        Field[] fields = clazz.getDeclaredFields();
        String params = "";
        for (Field field : fields) {
            params = params.concat(getParam(field));
        }
        return String.format(sql, table_name, params.substring(0, params.length() - 1));
    }

    public static <T extends DataSet> Object[] getFieldValues(T dataset) throws MyDBException {
        Class clazz = dataset.getClass();
        List<Field> fields = getFields(clazz);
        Object[] rez = new Object[fields.size()-1];
        int i = 0;
        for (Field field : fields) {
            if (!field.getName().toLowerCase().equals("id")) {
                field.setAccessible(true);
                try {
                    rez[i] = field.get(dataset);
                    i++;
                } catch (IllegalAccessException e) {
                    throw new MyDBException("Cannot get field value",e);
                }
            }
        }
        return rez;
    }


    public static List<Field> getFields(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> params = new ArrayList<>();
        if (clazz.getSuperclass() != Object.class) params.addAll(getFields(clazz.getSuperclass()));
        for (Field field : fields) {
            Class fiedlClass = field.getType();
            if (fiedlClass.isPrimitive() || fiedlClass.getSimpleName().equals("String")) {
                params.add(field);
            } else params.addAll(getFields(field.getType()));
        }
        return params;
    }

    private static String getParam(Field field) throws MyDBException {
        Class<?> type = field.getType();
        String result = "%s %s,";
        if ("int".equals(type.getSimpleName()) || "Integer".equals(type.getSimpleName())) {
            return String.format(result, field.getName(), "INT");
        } else if ("String".equals(type.getSimpleName())) {
            return String.format(result, field.getName(), "varchar(255)");
        } else if ("float".equals(type.getSimpleName()) || "Float".equals(type.getSimpleName())) {
            return String.format(result, field.getName(), "REAL");
        } else if ("double".equals(type.getSimpleName()) || "Double".equals(type.getSimpleName())) {
            return String.format(result, field.getName(), "DOUBLE");
        } else if ("boolean".equals(type.getSimpleName()) || "Boolean".equals(type.getSimpleName())) {
            return String.format(result, field.getName(), "BOOLEAN");
        } else if ("byte".equals(type.getSimpleName()) || "Byte".equals(type.getSimpleName())) {
            return String.format(result, field.getName(), "TINYINT");
        } else if ("short".equals(type.getSimpleName()) || "Short".equals(type.getSimpleName())) {
            return String.format(result, field.getName(), "SMALLINT");
        } else if ("long".equals(type.getSimpleName()) || "Long".equals(type.getSimpleName())) {
            return String.format(result, field.getName(), "BIGINT");
        }
        throw new MyDBException(String.format("Type %s is not supported!", type.getSimpleName()));

    }

    public static String getInsertSQL(Class clazz) {
        String sql = "insert into %s (%s) values (%s)";


        String table_name = clazz.getSimpleName().toLowerCase();
        Field[] fields = clazz.getDeclaredFields();

        String params = Arrays.stream(fields).map(Field::getName).collect(Collectors.joining(","));
        String values = Stream.generate(() -> "?").limit(fields.length).collect(Collectors.joining(","));

        sql = String.format(sql, table_name, params, values);
        return sql;
    }

    public static <T extends DataSet> String getSelectQuery(Class<T> clazz) {
        String sql = "SELECT %s FROM %s ;";
        String tableName = clazz.getSimpleName().toLowerCase();
        List<Field> fields = getFields(clazz);

        String params = fields.stream().map(Field::getName).collect(Collectors.joining(","));

        return String.format(sql,params, tableName);
    }
}
