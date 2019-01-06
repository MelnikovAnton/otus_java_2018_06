package ru.otus.hibernate;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hibernate.exceptions.MyDBException;
import ru.otus.hibernate.models.UserDataSet;
import ru.otus.hibernate.service.DBService;
import ru.otus.hibernate.service.DBServiceImpl;

public class MainMyORM {
    private static Logger logger = LoggerFactory.getLogger(MainMyORM.class);

    public static void main(String[] args) throws MyDBException {
        DBService dbService=new DBServiceImpl();
        logger.info(dbService.getMetadata());

        dbService.createTable(UserDataSet.class);

        dbService.save(new UserDataSet("Vasya Pupkin",21));
        dbService.save(new UserDataSet("Ivanov Ivan Ivanovich",51));

        logger.info("Find all:");
        logger.info(dbService.loadAll(UserDataSet.class).toString());

        logger.info("Find by ID:");
        logger.info(dbService.load(1,UserDataSet.class).toString());


        logger.info("Find by name:");
        logger.info(dbService.loadByName("Vasya Pupkin",UserDataSet.class).toString());

        logger.info("");

        logger.info(String.valueOf(dbService.getCount(UserDataSet.class)));

        dbService.truncateTable(UserDataSet.class);
        dbService.dropTable(UserDataSet.class);



    }
}
