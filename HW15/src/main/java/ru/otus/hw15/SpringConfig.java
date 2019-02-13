package ru.otus.hw15;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.hw15.dbService.service.DBService;
import ru.otus.hw15.dbService.service.DBServiceHibernateImpl;
import ru.otus.hw15.frontendService.TemplateProcessor;

import java.io.IOException;

@Configuration
@Import(MessageSystemConfig.class)
public class SpringConfig {


    @Bean
    public TemplateProcessor getTemplateProcessor() throws IOException {
        return new TemplateProcessor();
    }

    @Bean
    public DBService geDbService() {
        return new DBServiceHibernateImpl();
    }

// Message System


}
