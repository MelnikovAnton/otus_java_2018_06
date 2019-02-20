package ru.otus.hw15;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw15.dbService.service.DBService;
import ru.otus.hw15.messageSystem.Address;
import ru.otus.hw15.messageSystem.Addressee;
import ru.otus.hw15.messageSystem.MessageSystem;
import ru.otus.hw15.messageSystem.wraper.DbServiceWraper;
import ru.otus.hw15.messageSystem.wraper.FrontServiceWraper;
import ru.otus.hw15.messageSystem.messageUtils.Adresses;
import ru.otus.hw15.messageSystem.messageUtils.MessageSystemContext;

@Configuration
public class MessageSystemConfig {

    @Bean
    public MessageSystem getMessageSystem() {
        MessageSystem messageSystem = new MessageSystem();
        return messageSystem;
    }

    @Bean(name = "frontAddressee")
    public Addressee getFrontAddressee(MessageSystem messageSystem) {
        return new FrontServiceWraper(new Address(Adresses.FRONT_SERVICE.getName()),messageSystem);
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")//From SpringConfig.class
    @Bean(name = "dbAddressee")
    public Addressee getDbServiceAdressee(DBService dbService,MessageSystem messageSystem){
        return new DbServiceWraper(new Address(Adresses.DB_SERVICE.getName()),dbService,messageSystem);
    }

    @Bean
    public MessageSystemContext getMessageSystemContext(MessageSystem messageSystem,
                                                        Addressee frontAddressee,
                                                        Addressee dbAddressee) {
        MessageSystemContext context = new MessageSystemContext(messageSystem);
        context.setAddress(Adresses.DB_SERVICE.getName(),dbAddressee.getAddress());
        context.setAddress(Adresses.FRONT_SERVICE.getName(),frontAddressee.getAddress() );
        messageSystem.addAddressee(frontAddressee);
        messageSystem.addAddressee(dbAddressee);
        messageSystem.start();
        return context;
    }


}
