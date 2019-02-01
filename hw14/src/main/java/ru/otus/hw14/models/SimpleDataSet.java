package ru.otus.hw14.models;

import javax.persistence.Entity;

@Entity
public class SimpleDataSet extends DataSet {

    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "SimpleDataSet{id= " +getId() +
                " test='" + test + '\'' +
                '}';
    }
}
