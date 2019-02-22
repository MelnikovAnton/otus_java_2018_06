package ru.otus.hw15.messageSystem.messages.msgToDb;

import ru.otus.hw15.dbService.exceptions.MyDBException;
import ru.otus.hw15.dbService.models.UserDataSet;
import ru.otus.hw15.messageSystem.Address;
import ru.otus.hw15.messageSystem.Addressee;
import ru.otus.hw15.messageSystem.exceptions.MyMessageSystemException;
import ru.otus.hw15.messageSystem.messages.MsgToDB;
import ru.otus.hw15.messageSystem.messages.MsgToFront;
import ru.otus.hw15.messageSystem.messages.WsMessageType;
import ru.otus.hw15.messageSystem.wraper.DbServiceWraper;

public class GetByIdMsg extends MsgToDB {
    public GetByIdMsg(Address from, Address to, String data) {
        super(from, to, data);
    }

    @Override
    public void handleMsg(WsMessageType wsmsg, DbServiceWraper service, Addressee addressee) throws MyMessageSystemException {
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
}
