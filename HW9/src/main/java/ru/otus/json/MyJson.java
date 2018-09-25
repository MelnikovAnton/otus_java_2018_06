package ru.otus.json;

import javax.json.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unchecked"})
public class MyJson {

  //  private static final Set objects = new HashSet();
  //  private static final Set keys = new HashSet();


    public static JsonObject toJson(Object o) throws IllegalAccessException {
        JsonObjectBuilder jsonRootBuilder = Json.createObjectBuilder();
        if (o == null) return jsonRootBuilder.build();
        Class clazz = o.getClass();
   //     objects.add(o);
        for (Field f : clazz.getDeclaredFields()) {
            int modifiers = f.getModifiers();
       //     System.out.println(Modifier.toString(modifiers) + " " + f.getName());
boolean check = Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers) || "this$0".equals(f.getName());

            if (!check) {
                Class<?> type = f.getType();
                f.setAccessible(true);
                //  if (type.equals(String.class)) jsonBuilder=getJsonStringValue(jsonBuilder,f.getName(),f.get(o).toString());
                if (f.get(o) != null) {
                    if (type.isArray()) {
                        jsonRootBuilder = getJsonArrayValue(jsonRootBuilder, f, o);
                    } else {
                        addValue(jsonRootBuilder, f.getName(), f.get(o));
                    }
                }

            }
        }

        return jsonRootBuilder.build();
    }


    private static JsonObjectBuilder getJsonMapValue(Map map) throws IllegalAccessException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (Object key:map.keySet()){
            addValue(builder,key.toString(),map.get(key));
        }
        return builder;
    }

    private static JsonObjectBuilder getJsonArrayValue(JsonObjectBuilder builder, Field f, Object o) throws IllegalAccessException {
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        if (f == null) return builder.add("", JsonValue.NULL);
        for (int i = 0; i < Array.getLength(f.get(o)); i++) {
            addValue(arrBuilder, Array.get(f.get(o), i));
        }
        return builder.add(f.getName(), arrBuilder.build());
    }

    private static JsonObjectBuilder getJsonArrayValue(JsonObjectBuilder builder, String key, Collection collection) {
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        collection
                .forEach(x -> {
                    try {
                        addValue(arrBuilder, x);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        return builder.add(key, arrBuilder.build());
    }

    private static JsonArrayBuilder getJsonArrayValue(JsonArrayBuilder builder, Collection collection) {
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        collection
                .forEach(x -> {
                    try {
                        addValue(arrBuilder, x);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        return builder.add(arrBuilder.build());
    }


    private static JsonObjectBuilder addValue(JsonObjectBuilder builder, String key, Object value) throws IllegalAccessException {
        if (value instanceof Integer) {
            builder.add(key, (Integer) value);
            return builder;
        } else if (value instanceof String) {
            builder.add(key, (String) value);
            return builder;
        } else if (value instanceof Float) {
            builder.add(key, (Float) value);
            return builder;
        } else if (value instanceof Double) {
            builder.add(key, (Double) value);
            return builder;
        } else if (value instanceof Boolean) {
            builder.add(key, (Boolean) value);
            return builder;
        } else if (value instanceof Byte) {
            builder.add(key, (Byte) value);
            return builder;
        } else if (value instanceof Short) {
            builder.add(key, (Short) value);
            return builder;
        } else if (value instanceof Long) {
            builder.add(key, (Long) value);
            return builder;
        } else if (value instanceof JsonValue) {
            builder.add(key, (JsonValue) value);
            return builder;
        } else if (value instanceof Collection) {
            return getJsonArrayValue(builder, key, (Collection) value);
        } else if (value instanceof Map) {
            return builder.add(key,getJsonMapValue((Map) value));
        }

    //    return (checkUsed(value,key)) ? builder.add(key, toJson(value)) : builder;
        return builder.add(key, toJson(value));
    }

//    private static Boolean checkUsed(Object o,String key) {
//        if (objects.contains(o) && keys.contains(key)) {
//
//            System.out.println(o.getClass().getName() +"  =   "+ key);
//            return false;
//        }
//        objects.add(o);
//        keys.add(key);
//        return true;
//    }

    private static JsonArrayBuilder addValue(JsonArrayBuilder builder, Object value) throws IllegalAccessException {
        if (value instanceof Integer) {
            builder.add((Integer) value);
            return builder;
        } else if (value instanceof String) {
            builder.add((String) value);
            return builder;
        } else if (value instanceof Float) {
            builder.add((Float) value);
            return builder;
        } else if (value instanceof Double) {
            builder.add((Double) value);
            return builder;
        } else if (value instanceof Boolean) {
            builder.add((Boolean) value);
            return builder;
        } else if (value instanceof Byte) {
            builder.add((Byte) value);
            return builder;
        } else if (value instanceof Short) {
            builder.add((Short) value);
            return builder;
        } else if (value instanceof Long) {
            builder.add((Long) value);
            return builder;
        } else if (value instanceof JsonValue) {
            builder.add((JsonValue) value);
            return builder;
        } else if (value instanceof Collection) {
            return getJsonArrayValue(builder, (Collection) value);
        } else if (value instanceof Map) {
            return builder.add(getJsonMapValue((Map) value));
        }

      //  return (checkUsed(value,"")) ? builder.add(toJson(value)) : builder;
        return builder.add(toJson(value));

    }
}
