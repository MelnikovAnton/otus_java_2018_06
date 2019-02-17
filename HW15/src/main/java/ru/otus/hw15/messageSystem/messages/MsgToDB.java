package ru.otus.hw15.messageSystem.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import ru.otus.hw15.dbService.exceptions.MyDBException;
import ru.otus.hw15.dbService.models.PhoneDataSet;
import ru.otus.hw15.dbService.models.UserDataSet;
import ru.otus.hw15.frontendService.WsMessageType;
import ru.otus.hw15.messageSystem.Address;
import ru.otus.hw15.messageSystem.Addressee;
import ru.otus.hw15.messageSystem.wraper.DbServiceWraper;
import ru.otus.hw15.messageUtils.DataSetExclusionStrategy;

import java.util.List;

public class MsgToDB extends Message {

    private Gson GSON =new GsonBuilder()
            .setExclusionStrategies(new DataSetExclusionStrategy())
            .create();

    public MsgToDB(Address from, Address to, String data) {
        super(from, to, data);
        System.out.println(data);
    }

    @Override
    public void exec(Addressee addressee) {
        System.out.println("Exec MSG to DB " + Thread.currentThread().getName());
        DbServiceWraper service = (DbServiceWraper) addressee;
        WsMessageType wsmsg = GSON.fromJson(getJson(), WsMessageType.class);
        String messageType = wsmsg.getType();
        switch (messageType) {
            case "AddUserReq":
               addUser(wsmsg,service,addressee);
                break;
            case "GetByIdReq":
              getById(wsmsg,service,addressee);
                break;
            case "GetAllReq":
                getAll(wsmsg,service,addressee);
                break;
        }
    }

    private void getAll(WsMessageType wsmsg,DbServiceWraper service,Addressee addressee){


        List<UserDataSet> users = service.getService().loadAll(UserDataSet.class);

        WsMessageType wsresp = new WsMessageType();
        wsresp.setType("GetAllResp");
        wsresp.setSession(wsmsg.getSession());
        wsresp.setData(GSON.toJson(users));
        System.out.println(GSON.toJson(wsresp));
        MsgToForont resp = new MsgToForont(getTo(), getFrom(), GSON.toJson(wsresp));
        addressee.getMS().sendMessage(resp);
    }

    private void addUser(WsMessageType wsmsg,DbServiceWraper service,Addressee addressee){
        UserDataSet user = GSON.fromJson(wsmsg.getData(), UserDataSet.class);
        user.getAddress().setUserDataSet(user);
        List<PhoneDataSet> phones = user.getPhones();
        for (PhoneDataSet phone:phones){
            phone.setUserDataSet(user);
        }
        System.out.println("USER:"+user);
        try {
            service.getService().save(user);
        } catch (MyDBException e) {
            e.printStackTrace();
        }
        WsMessageType wsresp = new WsMessageType();
        wsresp.setType("AddUserResp");
        wsresp.setSession(wsmsg.getSession());
        wsresp.setData(GSON.toJson(user));
        MsgToForont resp = new MsgToForont(getTo(), getFrom(), GSON.toJson(wsresp));
        addressee.getMS().sendMessage(resp);
    }
    private void getById(WsMessageType wsmsg,DbServiceWraper service,Addressee addressee){
        String id = GSON.fromJson(wsmsg.getData(), String.class);
        System.out.println("ID: " + id);
        UserDataSet user = null;
        try {
            user = service.getService().load(Long.valueOf(id), UserDataSet.class);
        } catch (MyDBException e) {
            e.printStackTrace();
        }
        user=initializeAndUnproxy(user);
        WsMessageType wsresp = new WsMessageType();
        wsresp.setType("GetByIdResp");
        wsresp.setSession(wsmsg.getSession());
        wsresp.setData(GSON.toJson(user));
        System.out.println(GSON.toJson(wsresp));
        MsgToForont resp = new MsgToForont(getTo(), getFrom(), GSON.toJson(wsresp));
        addressee.getMS().sendMessage(resp);
    }

    public static <T> T initializeAndUnproxy(T entity) {
        if (entity == null) {
            throw new
                    NullPointerException("Entity passed for initialization is null");
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
                    .getImplementation();
        }
        return entity;
    }
}
