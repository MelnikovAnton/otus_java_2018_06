package ru.otus.web.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserDataSet extends DataSet {

    private String name;
    private int age;

    @OneToOne(mappedBy = "userDataSet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AddressDataSet address = new AddressDataSet(this);

    @OneToMany(mappedBy = "userDataSet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhoneDataSet> phones = new ArrayList<>();

    public UserDataSet(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public UserDataSet() {
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public List<PhoneDataSet> getPhones() {
        return phones;
    }

    private void setPhones(PhoneDataSet phone) {
        this.phones.add(phone);
    }


    @Override
    public String toString() {
        return "UserDataSet{Id= " + getId() +
                " name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }

    public void addPhone(String s) {
        PhoneDataSet phone = new PhoneDataSet(this);
        phone.setNumber(s);
        setPhones(phone);
    }
    public void setStreet(String s) {
        AddressDataSet address = new AddressDataSet(this);
        address.setStreet(s);
        setAddress(address);
    }
}
