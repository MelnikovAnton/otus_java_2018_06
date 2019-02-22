package ru.otus.hw15.frontendService;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.socket.server.standard.SpringConfigurator;
import ru.otus.hw15.messageSystem.exceptions.MyMessageSystemException;
import ru.otus.hw15.messageSystem.messages.Message;
import ru.otus.hw15.messageSystem.messages.MsgToDB;
import ru.otus.hw15.messageSystem.messages.WsMessageType;
import ru.otus.hw15.messageSystem.messages.msgToDb.AddUserMsg;
import ru.otus.hw15.messageSystem.messages.msgToDb.AllUserMsg;
import ru.otus.hw15.messageSystem.messages.msgToDb.GetByIdMsg;
import ru.otus.hw15.messageSystem.wraper.FrontServiceWraper;
import ru.otus.hw15.messageSystem.messageUtils.MessageSystemContext;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws", configurator = SpringConfigurator.class)
public class WebSocketFrontService implements FrontService {

    private static Logger logger = LoggerFactory.getLogger(WebSocketFrontService.class);

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
        logger.info("open-" + session.getId());
        this.session = session;
        frontAddressee.addService(this);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws MyMessageSystemException {
        logger.info("Message from the client " + session.getId() + " : " + message);
        WsMessageType wsmsg = GSON.fromJson(message, WsMessageType.class);
        wsmsg.setSession(session.getId());

        String type = wsmsg.getType();

        Message msg;
        switch (type) {
            case "AddUserReq":
                 msg = new AddUserMsg(context.getAddress("Front"),
                        context.getAddress("DB"),
                        GSON.toJson(wsmsg));
                break;
            case "GetByIdReq":
                 msg = new GetByIdMsg(context.getAddress("Front"),
                        context.getAddress("DB"),
                        GSON.toJson(wsmsg));
                break;
            case "GetAllReq":
                 msg = new AllUserMsg(context.getAddress("Front"),
                        context.getAddress("DB"),
                        GSON.toJson(wsmsg));
                break;
            default:
                throw new MyMessageSystemException("Message type " + type + " not supported");
        }

        context.getMessageSystem().sendMessage(msg);

    }

    @OnClose
    public void onClose(Session session) {
        logger.info("close " + session.getId());
        frontAddressee.removeService(session.getId());
    }

    @OnError
    public void onError(Session session,Throwable t){
        logger.warn("ERROR!!! " + session.getId());
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void sendMessage(String message) {
        logger.info("Sending message to WS "+ message);
        session.getAsyncRemote().sendText(message);
    }
}
