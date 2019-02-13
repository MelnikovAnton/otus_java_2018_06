package ru.otus.hw15.messageSystem.messages;

import com.google.gson.Gson;
import ru.otus.hw15.frontendService.FrontService;
import ru.otus.hw15.frontendService.WsMessageType;
import ru.otus.hw15.messageSystem.Address;
import ru.otus.hw15.messageSystem.Addressee;
import ru.otus.hw15.messageSystem.wraper.FrontServiceWraper;

public class MsgToForont extends Message {

    private static final Gson GSON = new Gson();

    public MsgToForont(Address from, Address to, String data) {
        super(from, to, data);
    }

    @Override
    public void exec(Addressee addressee) {
        System.out.println("Exec MSG to Front "+ Thread.currentThread().getName());
        System.out.println("Addressee "+ addressee);
        FrontServiceWraper wrapper = (FrontServiceWraper) addressee;
        System.out.println(getJson());
        WsMessageType msg = GSON.fromJson(getJson(), WsMessageType.class);
        FrontService service = wrapper.getService(msg.getSession());
        service.sendMessage(GSON.toJson(msg));
    }
}
