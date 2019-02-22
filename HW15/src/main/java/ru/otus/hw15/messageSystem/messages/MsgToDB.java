package ru.otus.hw15.messageSystem.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw15.dbService.exceptions.MyDBException;
import ru.otus.hw15.dbService.models.PhoneDataSet;
import ru.otus.hw15.dbService.models.UserDataSet;
import ru.otus.hw15.messageSystem.Address;
import ru.otus.hw15.messageSystem.Addressee;
import ru.otus.hw15.messageSystem.exceptions.MyMessageSystemException;
import ru.otus.hw15.messageSystem.wraper.DbServiceWraper;
import ru.otus.hw15.messageSystem.messageUtils.DataSetExclusionStrategy;

import java.util.List;

public abstract class MsgToDB extends Message {
    private static Logger logger = LoggerFactory.getLogger(MsgToDB.class);

    public Gson GSON = new GsonBuilder()
            .setExclusionStrategies(new DataSetExclusionStrategy())
            .create();

    public MsgToDB(Address from, Address to, String data) {
        super(from, to, data);
    }

    @Override
    public void exec(Addressee addressee) throws MyMessageSystemException {
        logger.info("Exec MSG to DB " + Thread.currentThread().getName());
        DbServiceWraper service = (DbServiceWraper) addressee;
        WsMessageType wsmsg = GSON.fromJson(getJson(), WsMessageType.class);
        handleMsg(wsmsg, service, addressee);
    }

    public abstract void handleMsg(WsMessageType wsmsg, DbServiceWraper service, Addressee addressee) throws MyMessageSystemException;


    public static <T> T initializeAndUnproxy(T entity) throws MyMessageSystemException {
        if (entity == null) {
            throw new
                    NullPointerException("Entity passed for initialization is null");
        }
        try {
            Hibernate.initialize(entity);
        } catch (Exception e) {
            throw new MyMessageSystemException("hibernate initialize exception", e);
        }
        if (entity instanceof HibernateProxy) {
            entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
                    .getImplementation();
        }
        return entity;
    }
}
