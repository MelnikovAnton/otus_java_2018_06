package ru.otus.myORM;


import ru.otus.myORM.exceptions.MyDBException;
import ru.otus.myORM.helpers.QueryBuilder;
import ru.otus.myORM.models.UserDataSet;
import ru.otus.myORM.service.DBService;
import ru.otus.myORM.service.impl.DBServiceImpl;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class Main {


    public static void main(String[] args) throws SQLException {

        DBService<UserDataSet> service = null;
        try {
            service = new DBServiceImpl<UserDataSet>(UserDataSet.class);
        } catch (MyDBException e) {
            e.printStackTrace();
        }
        System.out.println(service.getMetaData());


        service.deleteTables();

        try {
            service.prepareTable();
        } catch (MyDBException e) {
            e.printStackTrace();
        }



        service.save(new UserDataSet("one", 1));
        service.save(new UserDataSet("two", 2));
        service.save(new UserDataSet("three", 3));
        service.save(new UserDataSet("four", 4));

        System.out.println(QueryBuilder.getSelect(UserDataSet.class));

        try {
            List<UserDataSet> rez = service.getAll();
            Arrays.stream(rez.toArray())
                    .forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.println(service.getById(4L));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


    }

//    public Main() throws SQLException {
//        this.connection = DriverManager.getConnection(URL);
//        this.connection.setAutoCommit(false);
//    }
//
//    private void insertRecord(int id) throws SQLException {
//        try (PreparedStatement pst = connection.prepareStatement("insert into test(id, name) values (?, ?)")) {
//            Savepoint savePoint = this.connection.setSavepoint("savePointName");
//            pst.setInt(1, id);
//            pst.setString(2, "NameValue");
//            try {
//                int rowCount = pst.executeUpdate();
//                this.connection.commit();
//                System.out.println("inserted rowCount:" + rowCount);
//            } catch (SQLException ex) {
//                this.connection.rollback(savePoint);
//                System.out.println(ex.getMessage());
//            }
//        }
//    }
//
//    private void selectRecord(int id) throws SQLException {
//        try (PreparedStatement pst = this.connection.prepareStatement("select name from test where id  = ?")) {
//            pst.setInt(1, id);
//            try (ResultSet rs = pst.executeQuery()) {
//                System.out.print("name:");
//                if (rs.next()) {
//                    System.out.println(rs.getString("name"));
//                }
//            }
//        }
//    }
//
//    private void selectRecords() throws SQLException {
//        try (PreparedStatement pst = this.connection.prepareStatement("select * from test")) {
//            // pst.setInt(1, id);
//            try (ResultSet rs = pst.executeQuery()) {
//                System.out.print("name:");
//                while (rs.next()) {
//                    System.out.println(rs.getString("name"));
//                }
//            }
//        }
//    }
//
//
//    private void createTable() throws SQLException {
//        try (PreparedStatement pst = connection.prepareStatement("create table test(id int, name varchar(50))")) {
//            pst.executeUpdate();
//        }
//    }
//
//    public void close() throws SQLException {
//        this.connection.close();
//    }
}
