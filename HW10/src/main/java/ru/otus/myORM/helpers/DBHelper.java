package ru.otus.myORM.helpers;

import ru.otus.myORM.exceptions.MyDBException;
import ru.otus.myORM.models.DataSet;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DBHelper {
    public static String getCreateTableSQL(Class<? extends DataSet> clazz) throws MyDBException {
        String sql = "create table if not exists %s (id  SERIAL PRIMARY KEY, %s)";
        String table_name = clazz.getSimpleName().toLowerCase();
        Field[] fields = clazz.getDeclaredFields();
        String params = "";
        for (Field field : fields) {
            params = params.concat(getParam(field));
        }
        return String.format(sql,table_name,params.substring(0,params.length()-1));
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

    public static String getInsertSQL(DataSet dataSet) {
        String sql= "insert into %s (%s) values (%s)";
        Class clazz=dataSet.getClass();

        String table_name = clazz.getSimpleName().toLowerCase();
        Field[] fields = clazz.getDeclaredFields();

        String params = Arrays.stream(fields).map(Field::getName).collect(Collectors.joining(","));
        String values = Arrays.stream(fields).map(x->{
            x.setAccessible(true);
            Object val = null;
            try {
                val = x.get(dataSet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (val instanceof String) val="'" + val +"'";
            return val.toString();
        }).collect(Collectors.joining(","));

        sql=String.format(sql,table_name,params,values);
        System.out.println(sql);
        return sql;
    }
}
