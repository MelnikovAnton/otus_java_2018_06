package ru.otus.hw15.messageSystem.wraper;

import ru.otus.hw15.frontendService.FrontService;
import ru.otus.hw15.messageSystem.Address;
import ru.otus.hw15.messageSystem.Addressee;
import ru.otus.hw15.messageSystem.MessageSystem;

import java.util.*;

public class FrontServiceWraper implements Addressee {
    private final Address address;
    private final MessageSystem messageService;

    private final Map<String, FrontService> frontServices = new HashMap<String, FrontService>();

    public FrontServiceWraper(Address address, MessageSystem messageService) {
        this.address = address;
        this.messageService = messageService;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return messageService;
    }

    public FrontService getService(String id){
        return frontServices.get(id);
    }

    public void addService(FrontService frontService) {
        frontServices.put(frontService.getSession().getId(), frontService);
    }

    public void removeService(String id) {
        frontServices.remove(id);
    }

}
