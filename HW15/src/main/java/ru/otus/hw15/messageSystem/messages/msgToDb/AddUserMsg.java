package ru.otus.hw15.messageSystem.messages.msgToDb;

import ru.otus.hw15.dbService.exceptions.MyDBException;
import ru.otus.hw15.dbService.models.PhoneDataSet;
import ru.otus.hw15.dbService.models.UserDataSet;
import ru.otus.hw15.messageSystem.Address;
import ru.otus.hw15.messageSystem.Addressee;
import ru.otus.hw15.messageSystem.exceptions.MyMessageSystemException;
import ru.otus.hw15.messageSystem.messages.MsgToDB;
import ru.otus.hw15.messageSystem.messages.MsgToFront;
import ru.otus.hw15.messageSystem.messages.WsMessageType;
import ru.otus.hw15.messageSystem.wraper.DbServiceWraper;

import java.util.List;

public class AddUserMsg extends MsgToDB {
    public AddUserMsg(Address from, Address to, String data) {
        super(from, to, data);
    }

    @Override
    public void handleMsg(WsMessageType wsmsg, DbServiceWraper service, Addressee addressee) throws MyMessageSystemException {
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
}
