package ru.otus.hw15.messageSystem.wraper;

import ru.otus.hw15.dbService.service.DBService;
import ru.otus.hw15.messageSystem.Address;
import ru.otus.hw15.messageSystem.Addressee;
import ru.otus.hw15.messageSystem.MessageSystem;

import javax.annotation.PostConstruct;

public class DbServiceWraper implements Addressee {

    private final Address address;
    private final DBService dbService;
    private final MessageSystem messageService;

    public DbServiceWraper(Address address, DBService dbService, MessageSystem messageService) {
        this.address = address;
        this.dbService = dbService;
        this.messageService=messageService;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return messageService;
    }


    public DBService getService(){
        return dbService;
    }
}
