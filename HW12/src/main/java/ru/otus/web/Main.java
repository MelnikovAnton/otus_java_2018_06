package ru.otus.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.web.models.AddressDataSet;
import ru.otus.web.models.UserDataSet;
import ru.otus.web.service.DBService;
import ru.otus.web.service.DBServiceHibernateImpl;
import ru.otus.web.servlet.AddUserServlet;
import ru.otus.web.servlet.AllUsersServlet;
import ru.otus.web.servlet.GetByIdServlet;
import ru.otus.web.servlet.TemplateProcessor;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "tml";

    public static void main(String[] args) throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        DBService service = new DBServiceHibernateImpl();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();

        UserDataSet user = new UserDataSet("Vasya Pupkin", 21);
        AddressDataSet address = user.getAddress();
        address.setStreet("Lenina st");
        user.addPhone("1234567890");
        user.addPhone("0987654321");


        service.save(user);


        UserDataSet user1 = service.load(1, UserDataSet.class).get(0);
        logger.warn(user1.toString());

        UserDataSet user2 = new UserDataSet("Ivanov Ivan Ivanovich", 51);
        AddressDataSet address2 = user2.getAddress();
        address2.setStreet("Marksa st");
        user2.addPhone("1111111111");
        user2.addPhone("2222222222");
        service.save(user2);


        context.addServlet(new ServletHolder(new AllUsersServlet(templateProcessor,service)), "/allUsers");
        context.addServlet(new ServletHolder(new AddUserServlet(templateProcessor, service)), "/addUser");
        context.addServlet(new ServletHolder(new GetByIdServlet(templateProcessor, service)), "/getByID");

//       context.addServlet(AdminServlet.class, "/AllUsersServlet");
//        context.addServlet(TimerServlet.class, "/timer");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }
}
