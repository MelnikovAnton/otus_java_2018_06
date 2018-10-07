package ru.otus.myORM;


import ru.otus.myORM.exceptions.MyDBException;
import ru.otus.myORM.helpers.ConnectionHelper;
import ru.otus.myORM.models.DataSet;
import ru.otus.myORM.models.UserDataSet;
import ru.otus.myORM.service.DBService;
import ru.otus.myORM.service.impl.DBServiceImpl;

import java.sql.*;
import java.util.Arrays;

public class Main {
    private static final String URL = "jdbc:h2:mem:test";
    private final Connection connection;

    public static void main(String[] args) throws SQLException {
//        Main demo = new Main();
//
//        demo.createTable();
//        int id = 2;
//        for (int i=3;i<10;i++){
//            demo.insertRecord(id);
//        }
//
//        demo.selectRecords();
//    //    demo.close();

//==================
        DBService<UserDataSet> service= new DBServiceImpl<UserDataSet>(UserDataSet.class);
        System.out.println(service.getMetaData());

        try {
            service.prepareTable();
        } catch (MyDBException e) {
            e.printStackTrace();
        }

        service.add(Arrays.asList(new UserDataSet("one",1),
                new UserDataSet("two",2),
                new UserDataSet("three",3),
                new UserDataSet("four",4)));

//        try (PreparedStatement ps=ConnectionHelper.getConnection().prepareStatement("insert into userdataset(name,age) values (?, ?)")){
//            for (int i=10;i<15;i++){
//                ps.setString(1,"name"+i);
//                ps.setInt(2,i);
//                ps.executeUpdate();
//            }
//
//        }
//
//        ResultSet rs = ConnectionHelper.getConnection().prepareStatement("select * from userdataset").executeQuery();
//        while (rs.next()){
//            System.out.println(rs.getString("name"));
//        }


        service.getAll();


    }

    public Main() throws SQLException {
        this.connection = DriverManager.getConnection(URL);
        this.connection.setAutoCommit(false);
    }

    private void insertRecord(int id) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement("insert into test(id, name) values (?, ?)")) {
            Savepoint savePoint = this.connection.setSavepoint("savePointName");
            pst.setInt(1, id);
            pst.setString(2, "NameValue");
            try {
                int rowCount = pst.executeUpdate();
                this.connection.commit();
                System.out.println("inserted rowCount:" + rowCount);
            } catch (SQLException ex) {
                this.connection.rollback(savePoint);
                System.out.println(ex.getMessage());
            }
        }
    }

    private void selectRecord(int id) throws SQLException {
        try (PreparedStatement pst = this.connection.prepareStatement("select name from test where id  = ?")) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                System.out.print("name:");
                if (rs.next()) {
                    System.out.println(rs.getString("name"));
                }
            }
        }
    }

    private void selectRecords() throws SQLException {
        try (PreparedStatement pst = this.connection.prepareStatement("select * from test")) {
           // pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                System.out.print("name:");
                while (rs.next()) {
                    System.out.println(rs.getString("name"));
                }
            }
        }
    }


    private void createTable() throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement("create table test(id int, name varchar(50))")) {
            pst.executeUpdate();
        }
    }

    public void close() throws SQLException {
        this.connection.close();
    }
}
