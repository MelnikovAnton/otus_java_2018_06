package ru.otus.hw15.messageSystem.messages.msgToDb;

import ru.otus.hw15.dbService.models.UserDataSet;
import ru.otus.hw15.messageSystem.Address;
import ru.otus.hw15.messageSystem.Addressee;
import ru.otus.hw15.messageSystem.exceptions.MyMessageSystemException;
import ru.otus.hw15.messageSystem.messages.MsgToDB;
import ru.otus.hw15.messageSystem.messages.MsgToFront;
import ru.otus.hw15.messageSystem.messages.WsMessageType;
import ru.otus.hw15.messageSystem.wraper.DbServiceWraper;

import java.util.List;

public class AllUserMsg extends MsgToDB {
    public AllUserMsg(Address from, Address to, String data) {
        super(from, to, data);
    }

    @Override
    public void handleMsg(WsMessageType wsmsg, DbServiceWraper service, Addressee addressee) throws MyMessageSystemException {
        List<UserDataSet> users = service.getService().loadAll(UserDataSet.class);
        WsMessageType wsresp = new WsMessageType();
        wsresp.setType("GetAllResp");
        wsresp.setSession(wsmsg.getSession());
        wsresp.setData(GSON.toJson(users));
        MsgToFront resp = new MsgToFront(getTo(), getFrom(), GSON.toJson(wsresp));
        addressee.getMS().sendMessage(resp);
    }
}
