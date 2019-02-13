package ru.otus.hw15.dbService.DAO;

import org.hibernate.Session;
import ru.otus.hw15.dbService.models.DataSet;


import java.util.List;

public abstract class GeneralDao<T extends DataSet> {

    Session session;

    public GeneralDao(Session session) {
        this.session = session;
    }


    public abstract void save(T dataset);

    public abstract T read(long id,Class<T> clazz);

    public abstract List<T> readAll(Class<T> clazz);

    public abstract long count(Class<T> clazz);

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
