package ru.otus.hw14;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw14.cashe.CacheEngine;
import ru.otus.hw14.cashe.CacheEngineImpl;
import ru.otus.hw14.models.DataSet;
import ru.otus.hw14.service.DBService;
import ru.otus.hw14.service.DBServiceCache;
import ru.otus.hw14.service.DBServiceHibernateImpl;
import ru.otus.hw14.servlet.AddUserServlet;
import ru.otus.hw14.servlet.TemplateProcessor;

import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

@Configuration
public class SpringConfig {


    @Bean
    public TemplateProcessor getTemplateProcessor() throws IOException {
        return new TemplateProcessor();
    }

    @Bean
    public DBService geDbService(CacheEngine<Long, DataSet> cashe) {
        return new DBServiceCache(cashe);
    }

    @Bean
    public CacheEngine<Long, DataSet> getCacheEngine() {
        return new CacheEngineImpl<>(10, 0, 0, true);
    }

}
