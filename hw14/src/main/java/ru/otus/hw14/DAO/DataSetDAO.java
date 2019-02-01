package ru.otus.hw14.DAO;

import org.hibernate.Session;
import ru.otus.hw14.models.DataSet;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class DataSetDAO<T extends DataSet> extends GeneralDao<T> {


    public DataSetDAO(Session session) {
        super(session);
    }

    @Override
    public void save(T dataset) {
        session.save(dataset);
    }

    @Override
    public T read(long id,Class<T> clazz) {
        return (T) session.load(clazz, id);
    }


    @Override
    public List<T> readAll(Class<T> clazz) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        criteria.from(clazz);
        return (List<T>) session.createQuery(criteria).list();
    }

    @Override
    public long count(Class<T> clazz) {
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<T> countRoot = countCriteria.from(clazz);
        long total = session.createQuery(
                countCriteria.select(builder.count(countRoot))
        ).getSingleResult();
        return total;
    }


}
