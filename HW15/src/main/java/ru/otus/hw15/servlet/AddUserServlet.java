package ru.otus.hw15.servlet;


import ru.otus.hw15.exceptions.MyDBException;
import ru.otus.hw15.models.UserDataSet;
import ru.otus.hw15.service.DBService;

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

    private final TemplateProcessor templateProcessor;

    private final DBService service;

    public AddUserServlet(TemplateProcessor templateProcessor, DBService service) {
        this.templateProcessor = templateProcessor;
        this.service = service;
    }

    //    @Override
//    public void init(ServletConfig config) throws ServletException {
//        super.init(config);
//        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,config.getServletContext());
//    }


    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> values = new HashMap<>();

        Map<String, String[]> params = request.getParameterMap();
        if (!params.isEmpty()) {
            UserDataSet user = new UserDataSet();
            user.addPhone(params.get("phones")[0]);
            user.setStreet(params.get("street")[0]);
            try {
                user.setAge(Integer.parseInt(params.get("age")[0]));
            } catch (Exception e) {
                values.put("message", "Wrong age format!!!");
            }

            user.setName(params.get("name")[0]);

            try {
                service.save(user);
                values.put("user", user);
                values.put("message", "user added");
            } catch (MyDBException e) {
                values.put("message", "cannot add user");
                e.printStackTrace();
            }

        } else values.put("message", "");

        String page = templateProcessor.getPage(ADD_PAGE_TEMPLATE, values);//save to the page

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(page);
    }

}
