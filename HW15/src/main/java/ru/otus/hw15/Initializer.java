package ru.otus.hw15;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import ru.otus.hw15.frontendService.servlet.*;
import ru.otus.hw15.frontendService.servlet.filter.EncodingFilter;

import javax.servlet.*;

public class Initializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(SpringConfig.class);

        // Manage the lifecycle of the root application context
        servletContext.addListener(new ContextLoaderListener(rootContext));

        registerServlet(servletContext,"addUser","/addUser",new AddUserServlet());
        registerServlet(servletContext,"allUsers","/allUsers",new AllUsersServlet());
        registerServlet(servletContext,"getByID","/getByID",new GetByIdServlet());
        registerServlet(servletContext,"login","/login",new LoginServlet());



        FilterRegistration.Dynamic filter = servletContext.addFilter("encodingFilter", EncodingFilter.class);
        filter.addMappingForUrlPatterns(null,true,"/*");


    }

    private void registerServlet(ServletContext servletContext, String name, String mapping, Servlet servletInstance){
        ServletRegistration.Dynamic servlet =
                servletContext.addServlet(name, servletInstance);
        servlet.setLoadOnStartup(1);
        servlet.addMapping(mapping);
    }
}
