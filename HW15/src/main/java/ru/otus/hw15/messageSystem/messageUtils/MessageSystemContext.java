package ru.otus.hw15.messageSystem.messageUtils;

import ru.otus.hw15.messageSystem.exceptions.MyMessageSystemException;
import ru.otus.hw15.messageSystem.Address;
import ru.otus.hw15.messageSystem.MessageSystem;

import java.util.HashMap;
import java.util.Map;

public class MessageSystemContext {

    private final MessageSystem messageSystem;

    private final Map<String, Address> addresses=new HashMap<>();

    public MessageSystemContext(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    public MessageSystem getMessageSystem(){
        return messageSystem;
    }

    public Address getAddress(String name) throws MyMessageSystemException {
        if (!addresses.containsKey(name)) throw new MyMessageSystemException("No address whith name " + name);
        return addresses.get(name);
    }

    public void setAddress(String name,Address address){
        addresses.put(name,address);

    }

}
