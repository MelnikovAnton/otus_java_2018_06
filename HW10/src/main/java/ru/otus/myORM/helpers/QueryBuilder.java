package ru.otus.myORM.helpers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    public static String getSelect(Class clazz){
        String sql = "SELECT %s FROM %s;";
     //   System.out.println(String.join(" ",getFields(clazz)));
        return String.format(sql,String.join(",",getFields(clazz)),clazz.getSimpleName());
    }

    private static List<String> getFields(Class clazz){
        Field[] fields = clazz.getDeclaredFields();
        List<String> params=new ArrayList<>();
        if (clazz.getSuperclass() != Object.class) params.addAll(getFields(clazz.getSuperclass()));
        for (Field field:fields){
            Class fiedlClass = field.getType();
            if (fiedlClass.isPrimitive() || fiedlClass.getSimpleName().equals("String")) {
                params.add("\""+field.getName()+"\"");
            } else params.addAll(getFields(field.getType()));
        }
        return params;
    }

    public static String getSelectByID(Class clazz) {
        String sql = "SELECT %s FROM %s where id = ?;";
        //   System.out.println(String.join(" ",getFields(clazz)));
        return String.format(sql,String.join(",",getFields(clazz)),clazz.getSimpleName());
    }
}
