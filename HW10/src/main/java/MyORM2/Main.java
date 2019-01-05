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

        System.out.println(dbService.load(0,UserDataSet.class));

//             dbService.truncateTable(UserDataSet.class);
//        dbService.dropTable(UserDataSet.class);



    }
}
