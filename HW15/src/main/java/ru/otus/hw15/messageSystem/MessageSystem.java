package ru.otus.hw15.messageSystem;

import ru.otus.hw15.messageSystem.exceptions.MyMessageSystemException;
import ru.otus.hw15.messageSystem.messages.Message;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class MessageSystem {
    private final static Logger logger = Logger.getLogger(MessageSystem.class.getName());

    private final Map<Address, Thread> workers;
    private final Map<Address, LinkedBlockingQueue<Message>> messagesMap;
    private final Map<Address, Addressee> addresseeMap;

    public MessageSystem() {
        workers = new HashMap<>();
        messagesMap = new HashMap<>();
        addresseeMap = new HashMap<>();
    }

    public void addAddressee(Addressee addressee) {
        addresseeMap.put(addressee.getAddress(), addressee);
        messagesMap.put(addressee.getAddress(), new LinkedBlockingQueue<>());
    }

    public void sendMessage(Message message) {
        messagesMap.get(message.getTo()).add(message);
    }


    public void start() {

        for (Map.Entry<Address, Addressee> entry : addresseeMap.entrySet()) {

            String name = "MS-worker-" + entry.getKey().getId();
            Thread thread = new Thread(run(entry));
//            Thread thread = new Thread(() -> {
//                LinkedBlockingQueue<Message> queue = messagesMap.get(entry.getKey());
//                while (true) {
//                    try {
//                        Message message = queue.take();
//                        message.exec(entry.getValue());
//                    } catch (InterruptedException e) {
//                        logger.log(Level.INFO, "Thread interrupted. Finishing: " + name);
//                        return;
//                    } catch (MyMessageSystemException e) {
//                        logger.log(Level.WARNING,"Error in exec message",e);
//                    }
//                }
//            });
            thread.setName(name);
            thread.start();
            workers.put(entry.getKey(), thread);

        }

        startMonitor();
    }

    private void startMonitor() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(getMonitor(), 10000, 10000);

    }

    private TimerTask getMonitor() {
        return new TimerTask() {
            @Override
            public void run() {
                workers.forEach((key, value) -> {
         //           logger.log(Level.INFO,value.getState().name());
                    if ("TERMINATED".equals(value.getState().name())) {
                        logger.log(Level.WARNING, "Thread " + value.getName() + " is terminated!");
                        Map.Entry<Address, Addressee> addresseeEntry = addresseeMap.entrySet().stream()
                                .filter(e -> key.equals(e.getKey()))
                                .findFirst().get();

                        String name = "MS-worker-" + key.getId();
                        Thread thread = new Thread(MessageSystem.this.run(addresseeEntry));
                        thread.setName(name);
                        thread.start();
                        workers.put(key, thread);
                        logger.log(Level.WARNING, "Thread " + value.getName() + " was restarted!");
                    }
                });
            }
        };
    }


    private Runnable run(Map.Entry<Address, Addressee> entry) {
        return () -> {
            LinkedBlockingQueue<Message> queue = messagesMap.get(entry.getKey());
            String name = "MS-worker-" + entry.getKey().getId();
            while (true) {
                try {
                    Message message = queue.take();
                    message.exec(entry.getValue());
                } catch (InterruptedException e) {
                    logger.log(Level.INFO, "Thread interrupted. Finishing: " + name);
                    return;
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error in exec message", e);
                }
            }
        };
    }

    public void dispose() {
        workers.values().forEach(Thread::interrupt);
    }
}
