package ru.otus.hw15.messageSystem.messages;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw15.frontendService.FrontService;
import ru.otus.hw15.messageSystem.Address;
import ru.otus.hw15.messageSystem.Addressee;
import ru.otus.hw15.messageSystem.wraper.FrontServiceWraper;

public class MsgToFront extends Message {

    private static Logger logger = LoggerFactory.getLogger(MsgToFront.class);

    private static final Gson GSON = new Gson();

    public MsgToFront(Address from, Address to, String data) {
        super(from, to, data);
    }

    @Override
    public void exec(Addressee addressee) {
        logger.info("Exec MSG to Front "+ Thread.currentThread().getName());
        FrontServiceWraper wrapper = (FrontServiceWraper) addressee;
        WsMessageType msg = GSON.fromJson(getJson(), WsMessageType.class);
        FrontService service = wrapper.getService(msg.getSession());
        if (service!=null) service.sendMessage(GSON.toJson(msg));
    }
}
