package ru.otus.hibernate.myORM.models;

public abstract class DataSet {
    private long id;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
