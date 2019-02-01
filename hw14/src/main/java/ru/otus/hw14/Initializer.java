package ru.otus.hw14;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import ru.otus.hw14.servlet.AddUserServlet;
import ru.otus.hw14.servlet.AllUsersServlet;
import ru.otus.hw14.servlet.GetByIdServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class Initializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(SpringConfig.class);

        // Manage the lifecycle of the root application context
        servletContext.addListener(new ContextLoaderListener(rootContext));

        ServletRegistration.Dynamic addUser =
                servletContext.addServlet("addUser", new AddUserServlet());
        addUser.setLoadOnStartup(1);
        addUser.addMapping("/addUser");

        ServletRegistration.Dynamic allUsers =
                servletContext.addServlet("allUsers", new AllUsersServlet());
        allUsers.setLoadOnStartup(1);
        allUsers.addMapping("/allUsers");

        ServletRegistration.Dynamic getByID =
                servletContext.addServlet("getByID", new GetByIdServlet());
        getByID.setLoadOnStartup(1);
        getByID.addMapping("/getByID");


    }
}
