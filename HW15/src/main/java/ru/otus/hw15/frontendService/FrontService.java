package ru.otus.hw15.frontendService;

import javax.websocket.Session;

public interface FrontService  {
    Session getSession();
    void sendMessage(String message);
}
