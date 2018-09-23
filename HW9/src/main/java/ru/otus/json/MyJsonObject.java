package ru.otus.json;

import ch.qos.logback.core.db.dialect.PostgreSQLDialect;
import com.google.gson.Gson;
import com.google.gson.internal.Excluder;

import java.util.*;

public class MyJsonObject {
    private int intVal =5;
    private Integer integerVal =10;
    private String stringVal = "str";
    private int[] intArrVal ={1,2,3,4,5};
    private String[] stringArrVal = {"1","2","3"};

    private String nullVal;

 //   private Excluder excluder=new Excluder();
    private Object obj= new Object();

    private Map<String,String> map = new HashMap<>();

    private List<String> strList = new ArrayList<>();

    private SubClass subObj = new SubClass();

    private List<List<String>> listList = new ArrayList<>();

    public MyJsonObject(List<String> list){
        this.strList.addAll(list);
        map.put("one","1");
        this.listList.add(list);
        this.listList.add(list);
    }


    private final class SubClass{
        private String str="sub string";
        private int i=5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyJsonObject that = (MyJsonObject) o;
        return intVal == that.intVal &&
                Objects.equals(integerVal, that.integerVal) &&
                Objects.equals(stringVal, that.stringVal) &&
                Arrays.equals(intArrVal, that.intArrVal) &&
                Arrays.equals(stringArrVal, that.stringArrVal) &&
                Objects.equals(nullVal, that.nullVal) &&
                Objects.equals(obj, that.obj) &&
                Objects.equals(map, that.map) &&
                Objects.equals(strList, that.strList) &&
                Objects.equals(subObj, that.subObj) &&
                Objects.equals(listList, that.listList);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(intVal, integerVal, stringVal, nullVal, obj, map, strList, subObj, listList);
        result = 31 * result + Arrays.hashCode(intArrVal);
        result = 31 * result + Arrays.hashCode(stringArrVal);
        return result;
    }
}
