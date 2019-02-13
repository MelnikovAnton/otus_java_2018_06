package ru.otus.hw15.frontendService.servlet;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.hw15.dbService.exceptions.MyDBException;
import ru.otus.hw15.dbService.models.UserDataSet;
import ru.otus.hw15.dbService.service.DBService;
import ru.otus.hw15.frontendService.TemplateProcessor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllUsersServlet extends HttpServlet {

    private static final String LOGIN_PAGE_TEMPLATE = "allUsers.html";

    @Autowired
    private TemplateProcessor templateProcessor;
    @Autowired
    private  DBService service;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,config.getServletContext());
    }

//    public AllUsersServlet(TemplateProcessor templateProcessor, DBService service) {
//        this.templateProcessor = templateProcessor;
//        this.service=service;
//    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {

        List<UserDataSet> users = null;
        long count = 0;
        try {
            users = service.loadAll(UserDataSet.class);
            count = service.getCount(UserDataSet.class);
        } catch (MyDBException e) {
            e.printStackTrace();
        }

        Map<String, Object> values = new HashMap<>();
        values.put("users", users);
        values.put("count", count);

        String page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, values);//save to the page

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(page);
    }

}
