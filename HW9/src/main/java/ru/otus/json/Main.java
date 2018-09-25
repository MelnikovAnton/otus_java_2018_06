package ru.otus.json;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws IllegalAccessException {
        logger.debug("Main");
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("2");

        MyJsonObject obj = new MyJsonObject(list);

        Gson gson = new Gson();
        String str = gson.toJson(obj);
        logger.info(str);

        javax.json.JsonObject json = MyJson.toJson(obj);

        logger.debug(json.toString());


    }
}
