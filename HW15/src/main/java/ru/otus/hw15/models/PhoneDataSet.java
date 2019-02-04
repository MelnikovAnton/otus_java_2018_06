package ru.otus.hw15.models;

import javax.persistence.*;

@Entity
public class PhoneDataSet extends DataSet {
    private String number;

    @ManyToOne
    private UserDataSet userDataSet;

    public PhoneDataSet(UserDataSet userDataSet) {
        this.userDataSet=userDataSet;
    }

    public PhoneDataSet() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public UserDataSet getUserDataSet() {
        return userDataSet;
    }

    public void setUserDataSet(UserDataSet userDataSet) {
        this.userDataSet = userDataSet;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "number='" + number + '\'' +
                '}';
    }
}
