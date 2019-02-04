package ru.otus.hw15.service;

import org.hibernate.LazyInitializationException;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw15.cashe.CacheEngine;
import ru.otus.hw15.exceptions.MyDBException;
import ru.otus.hw15.models.DataSet;


public class DBServiceCache extends DBServiceHibernateImpl {

    private static Logger logger = LoggerFactory.getLogger(DBServiceCache.class);

    private final CacheEngine<Long, DataSet> cashe;

    public DBServiceCache(CacheEngine<Long, DataSet> cashe) {
        this.cashe = cashe;
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) throws MyDBException {
        T result = (T) cashe.get(id);
        if (result != null) return result;
        result = super.load(id, clazz);
        try {
            result.getId();
            cashe.put(id, result);
            logger.info("cashe updated " + result.toString());
        } catch (LazyInitializationException| ObjectNotFoundException e){
            result=null;
        }


        return result;
    }
}
