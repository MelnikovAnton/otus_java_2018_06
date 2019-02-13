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
import java.util.Map;


public class AddUserServlet extends HttpServlet {
    private static final String ADD_PAGE_TEMPLATE = "addUser.html";


    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private  DBService service;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,config.getServletContext());
    }


    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        Map<String, Object> values = new HashMap<>();

        Map<String, String[]> params = request.getParameterMap();
        if (!params.isEmpty()) {
            UserDataSet user = new UserDataSet();
            user.addPhone(params.get("phones")[0]);
            user.setStreet(params.get("street")[0]);
            try {
                user.setAge(Integer.parseInt(params.get("age")[0]));
            } catch (Exception e) {
                values.put("messageSystem", "Wrong age format!!!");
            }

            user.setName(params.get("name")[0]);

            try {
                service.save(user);
                values.put("user", user);
                values.put("messageSystem", "user added");
            } catch (MyDBException e) {
                values.put("messageSystem", "cannot add user");
                e.printStackTrace();
            }

        } else values.put("messageSystem", "");

        String page = templateProcessor.getPage(ADD_PAGE_TEMPLATE, values);//save to the page

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(page);
    }

}
