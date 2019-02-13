package ru.otus.hw15.messageSystem.messages;

import com.google.gson.Gson;
import ru.otus.hw15.dbService.exceptions.MyDBException;
import ru.otus.hw15.dbService.models.PhoneDataSet;
import ru.otus.hw15.dbService.models.UserDataSet;
import ru.otus.hw15.frontendService.WsMessageType;
import ru.otus.hw15.messageSystem.Address;
import ru.otus.hw15.messageSystem.Addressee;
import ru.otus.hw15.messageSystem.wraper.DbServiceWraper;

import java.util.List;

public class MsgToDB extends Message {

    private static final Gson GSON = new Gson();

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
        }
    }

    private void addUser(WsMessageType wsmsg,DbServiceWraper service,Addressee addressee){
        UserDataSet user = GSON.fromJson(wsmsg.getData(), UserDataSet.class);
        System.out.println(user);
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
        WsMessageType wsresp = new WsMessageType();
        wsresp.setType("GetByIdResp");
        wsresp.setSession(wsmsg.getSession());
        user.getAddress();
        List<PhoneDataSet> phones = user.getPhones();
        for (PhoneDataSet phone:phones) {
            phone.getNumber();
        }
        wsresp.setData(GSON.toJson(user));
        System.out.println(GSON.toJson(wsresp));
        MsgToForont resp = new MsgToForont(getTo(), getFrom(), GSON.toJson(wsresp));
        addressee.getMS().sendMessage(resp);
    }
}
