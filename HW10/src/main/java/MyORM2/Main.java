package MyORM2;

import MyORM2.exceptions.MyDBException;
import MyORM2.models.UserDataSet;
import MyORM2.service.DBService;
import MyORM2.service.DBServiceImpl;

public class Main {

    public static void main(String[] args) throws MyDBException {
        DBService dbService=new DBServiceImpl();
        System.out.println(dbService.getMetadata());

        dbService.createTable(UserDataSet.class);

        dbService.save(new UserDataSet("Vasya Pupkin",21));
        dbService.save(new UserDataSet("Ivanov Ivan Ivanovich",51));

        System.out.println("Find all:");
        System.out.println(dbService.loadAll(UserDataSet.class));

        System.out.println("Find by ID:");
        System.out.println(dbService.load(1,UserDataSet.class));


        System.out.println("Find by name:");
        System.out.println(dbService.loadByName("Vasya Pupkin",UserDataSet.class));

        System.out.println("");

        dbService.truncateTable(UserDataSet.class);
        dbService.dropTable(UserDataSet.class);



    }
}
