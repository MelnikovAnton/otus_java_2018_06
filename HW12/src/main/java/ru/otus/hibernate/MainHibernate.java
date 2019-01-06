package ru.otus.hibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hibernate.exceptions.MyDBException;
import ru.otus.hibernate.models.AddressDataSet;
import ru.otus.hibernate.models.PhoneDataSet;
import ru.otus.hibernate.models.SimpleDataSet;
import ru.otus.hibernate.models.UserDataSet;
import ru.otus.hibernate.service.DBService;
import ru.otus.hibernate.service.DBServiceHibernateImpl;

import java.util.ArrayList;
import java.util.List;

public class MainHibernate {
    private static Logger logger = LoggerFactory.getLogger(MainHibernate.class);

    public static void main(String[] args) throws MyDBException {

       try (DBService service = new DBServiceHibernateImpl();) {
           logger.warn(service.getMetadata());

           UserDataSet user = new UserDataSet("Vasya Pupkin", 21);
           AddressDataSet address = user.getAddress();
           address.setStreet("Lenina st");
           user.addPhone("1234567890");
           user.addPhone("0987654321");


           service.save(user);


           UserDataSet user1 = service.load(1, UserDataSet.class).get(0);
           logger.warn(user1.toString());

           UserDataSet user2 = new UserDataSet("Ivanov Ivan Ivanovich", 51);
           AddressDataSet address2 = user2.getAddress();
           address2.setStreet("Marksa st");
           user2.addPhone("1111111111");
           user2.addPhone("2222222222");
           service.save(user2);


           logger.warn(service.loadAll(UserDataSet.class).toString());

           logger.warn(service.loadByName("Vasya Pupkin",UserDataSet.class).toString());


           logger.warn(String.valueOf(service.getCount(UserDataSet.class)));

           SimpleDataSet simple = new SimpleDataSet();
           simple.setTest("TEST!!!");

           service.save(simple);

           logger.warn(service.load(1,SimpleDataSet.class).toString());


       } catch (Exception e) {
           e.printStackTrace();
       }


    }
}
