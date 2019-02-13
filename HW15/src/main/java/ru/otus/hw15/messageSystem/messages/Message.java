package ru.otus.hw15.messageSystem.messages;

import ru.otus.hw15.messageSystem.Address;
import ru.otus.hw15.messageSystem.Addressee;

public abstract class Message {
    private final Address from;
    private final Address to;
    private final String json;


    public Message(Address from, Address to, String json) {
        this.from = from;
        this.to = to;
        this.json = json;
    }

    public Address getFrom() {
        return from;
    }

    public Address getTo() {
        return to;
    }

    public String getJson() {
        return json;
    }

    public abstract void exec(Addressee addressee);
}
