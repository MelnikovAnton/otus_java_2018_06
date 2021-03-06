package ru.otus.hw14.DAO;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.otus.hw14.models.UserDataSet;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UserDAO extends DataSetDAO<UserDataSet> {

    public UserDAO(Session session) {
        super(session);
    }

    public UserDataSet readByName(String name) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        Root<UserDataSet> from = criteria.from(UserDataSet.class);
        criteria.where(builder.equal(from.get("name"), name));
        Query<UserDataSet> query = session.createQuery(criteria);
        return query.uniqueResult();
    }

}
