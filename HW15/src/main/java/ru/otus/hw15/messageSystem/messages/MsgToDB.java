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

public class MsgToDB extends Message {
    private static Logger logger = LoggerFactory.getLogger(MsgToDB.class);

    private Gson GSON = new GsonBuilder()
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
        String messageType = wsmsg.getType();
        switch (messageType) {
            case "AddUserReq":
                addUser(wsmsg, service, addressee);
                break;
            case "GetByIdReq":
                getById(wsmsg, service, addressee);
                break;
            case "GetAllReq":
                getAll(wsmsg, service, addressee);
                break;
            default:
                throw new MyMessageSystemException("Message type " + messageType + " not supported");
        }
    }

    private void getAll(WsMessageType wsmsg, DbServiceWraper service, Addressee addressee) {


        List<UserDataSet> users = service.getService().loadAll(UserDataSet.class);

        WsMessageType wsresp = new WsMessageType();
        wsresp.setType("GetAllResp");
        wsresp.setSession(wsmsg.getSession());
        wsresp.setData(GSON.toJson(users));
        MsgToFront resp = new MsgToFront(getTo(), getFrom(), GSON.toJson(wsresp));
        addressee.getMS().sendMessage(resp);
    }

    private void addUser(WsMessageType wsmsg, DbServiceWraper service, Addressee addressee) throws MyMessageSystemException {
        UserDataSet user = GSON.fromJson(wsmsg.getData(), UserDataSet.class);
        user.getAddress().setUserDataSet(user);
        List<PhoneDataSet> phones = user.getPhones();
        for (PhoneDataSet phone : phones) {
            phone.setUserDataSet(user);
        }
        try {
            service.getService().save(user);
        } catch (MyDBException e) {
            throw new MyMessageSystemException("Add user data base exception", e);
        }
        WsMessageType wsresp = new WsMessageType();
        wsresp.setType("AddUserResp");
        wsresp.setSession(wsmsg.getSession());
        wsresp.setData(GSON.toJson(user));
        MsgToFront resp = new MsgToFront(getTo(), getFrom(), GSON.toJson(wsresp));
        addressee.getMS().sendMessage(resp);
    }

    private void getById(WsMessageType wsmsg, DbServiceWraper service, Addressee addressee) throws MyMessageSystemException {
        String id = GSON.fromJson(wsmsg.getData(), String.class);
        UserDataSet user = null;
        try {
            user = service.getService().load(Long.valueOf(id), UserDataSet.class);
        } catch (MyDBException e) {
            throw new MyMessageSystemException("get user data base exception", e);
        }
        user = initializeAndUnproxy(user);
        WsMessageType wsresp = new WsMessageType();
        wsresp.setType("GetByIdResp");
        wsresp.setSession(wsmsg.getSession());
        wsresp.setData(GSON.toJson(user));
        MsgToFront resp = new MsgToFront(getTo(), getFrom(), GSON.toJson(wsresp));
        addressee.getMS().sendMessage(resp);
    }

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
