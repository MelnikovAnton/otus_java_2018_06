package ru.otus.myORM.models;

public class UserDataSet extends DataSet {

    private String name;
    private int age;

    public UserDataSet(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
