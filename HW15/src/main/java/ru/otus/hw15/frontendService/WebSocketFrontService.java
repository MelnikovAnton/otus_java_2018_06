package ru.otus.hw15.frontendService;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.socket.server.standard.SpringConfigurator;
import ru.otus.hw15.frontendService.exceptions.MyMessageSystemException;
import ru.otus.hw15.messageSystem.messages.Message;
import ru.otus.hw15.messageSystem.messages.MsgToDB;
import ru.otus.hw15.messageSystem.wraper.FrontServiceWraper;
import ru.otus.hw15.messageUtils.MessageSystemContext;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws", configurator = SpringConfigurator.class)
public class WebSocketFrontService implements FrontService {

    private final static Gson GSON=new Gson();

    @Autowired
    private FrontServiceWraper frontAddressee;


    @Autowired
    private MessageSystemContext context;

    private Session session;

    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }


    @OnOpen
    public void onOpen(Session session) {
        System.out.println("open-" + session.getId());
        this.session = session;
        frontAddressee.addService(this);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws MyMessageSystemException {
        System.out.println("Message from the client " + session.getId() + " : " + message);
        WsMessageType wsmsg = GSON.fromJson(message, WsMessageType.class);
        wsmsg.setSession(session.getId());

        Message msg = new MsgToDB(context.getAddress("Front"),
                context.getAddress("DB"),
                GSON.toJson(wsmsg));

        context.getMessageSystem().sendMessage(msg);

    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("close " + session.getId());
        frontAddressee.removeService(session.getId());
    }

    @OnError
    public void onError(Session session,Throwable t){
        System.out.println("ERROR!!! " + session.getId());
        t.printStackTrace();
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void sendMessage(String message) {
        System.out.println("Sending message to WS "+ message);
        session.getAsyncRemote().sendText(message);
    }
}
