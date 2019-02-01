package ru.otus.hw14.models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class AddressDataSet extends DataSet {

    private String street;

    @OneToOne
    private UserDataSet userDataSet;

    public AddressDataSet(UserDataSet userDataSet) {
        this.userDataSet = userDataSet;
    }

    public AddressDataSet() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public UserDataSet getUserDataSet() {
        return userDataSet;
    }

    public void setUserDataSet(UserDataSet userDataSet) {
        this.userDataSet = userDataSet;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "street='" + street + '\'' +
                '}';
    }
}
