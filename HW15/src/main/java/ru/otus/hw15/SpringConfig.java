package ru.otus.hw15;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.hw15.dbService.service.DBService;
import ru.otus.hw15.dbService.service.DBServiceHibernateImpl;

@Configuration
@Import(MessageSystemConfig.class)
public class SpringConfig {



    @Bean
    public DBService geDbService() {
        return new DBServiceHibernateImpl();
    }
}
