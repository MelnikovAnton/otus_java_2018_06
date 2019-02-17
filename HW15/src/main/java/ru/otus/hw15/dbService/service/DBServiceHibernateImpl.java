package ru.otus.hw15.dbService.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw15.dbService.DAO.DataSetDAO;
import ru.otus.hw15.dbService.DAO.UserDAO;
import ru.otus.hw15.dbService.exceptions.MyDBException;
import ru.otus.hw15.dbService.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class DBServiceHibernateImpl implements DBService {
    private static Logger logger = LoggerFactory.getLogger(DBServiceHibernateImpl.class);

    private final SessionFactory sessionFactory;

    public DBServiceHibernateImpl() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(AddressDataSet.class);
        configuration.addAnnotatedClass(PhoneDataSet.class);
        configuration.addAnnotatedClass(SimpleDataSet.class);


//        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
//        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:");
//        configuration.setProperty("hibernate.connection.username", "sa");
//        configuration.setProperty("hibernate.connection.password", "");

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL94Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://127.0.0.1:5432/test");
        configuration.setProperty("hibernate.connection.username", "ivr");
        configuration.setProperty("hibernate.connection.password", "ivr");

        configuration.setProperty("hibernate.show_sql", "false");
        configuration.setProperty("hibernate.format_sql", "false");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans","true");


        sessionFactory = createSessionFactory(configuration);
    }

    @Override
    public <T extends DataSet> void save(T user) {
         runInSession(session -> {
            DataSetDAO<T> dao = new DataSetDAO(session);
            dao.save(user);
            logger.warn(user.toString());
            return null;
        });
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) throws MyDBException {
        return runInSession(session -> {
            DataSetDAO<T> dao = new DataSetDAO(session);
            return dao.read(id,clazz);
        });
    }

    @Override
    public <T extends DataSet> List<T> loadByName(String name, Class<T> clazz) {
        List<T> rez= new ArrayList<>();
        UserDataSet obj = runInSession(session -> {
            UserDAO dao = new UserDAO(session);
            return dao.readByName(name);
        });
        rez.add((T) obj);
        return rez;
    }

    @Override
    public <T extends DataSet> List<T> loadAll(Class<T> clazz) {
        return runInSession(session -> {
            DataSetDAO<T> dao = new DataSetDAO(session);
            return dao.readAll(clazz);
        });
    }



    @Override
    public void truncateTable(Class clazz) throws MyDBException {
        throw new MyDBException("Dont need for Hibernate impl");
    }

    @Override
    public void dropTable(Class clazz) throws MyDBException {
        throw new MyDBException("Dont need for Hibernate impl");
    }

    @Override
    public void createTable(Class clazz) throws MyDBException {
throw new MyDBException("Dont need for Hibernate impl");
    }

    @Override
    public String getMetadata() {
        return runInSession(session -> {
            AtomicReference<String> r = new AtomicReference<>();
            session.doWork(work -> r.set("Connected to: " + work.getMetaData().getURL() + "\n" +
                    "DB name: " + work.getMetaData().getDatabaseProductName() + "\n" +
                    "DB version: " + work.getMetaData().getDatabaseProductVersion() + "\n" +
                    "Driver: " + work.getMetaData().getDriverName() + "\n" +
                    "Schema: " + work.getSchema()));
            return r.get();
        });
    }

    @Override
    public <T extends DataSet> long getCount(Class<T> clazz) {
        return runInSession(session -> {
            DataSetDAO<T> dao = new DataSetDAO(session);
            return dao.count(clazz);
        });
    }


    @Override
    public void close() {
        sessionFactory.close();
    }


    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }
}
