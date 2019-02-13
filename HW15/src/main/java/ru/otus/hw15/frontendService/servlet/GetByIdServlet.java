package ru.otus.hw15.frontendService.servlet;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.hw15.dbService.models.UserDataSet;
import ru.otus.hw15.dbService.service.DBService;
import ru.otus.hw15.frontendService.TemplateProcessor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetByIdServlet extends HttpServlet {
    private static final String GET_PAGE_TEMPLATE = "getById.html";

    @Autowired
    private TemplateProcessor templateProcessor;
    @Autowired
    private DBService service;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

//    public GetByIdServlet(TemplateProcessor templateProcessor, DBService service) {
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
        Map<String, Object> values = new HashMap<>();

        Map<String, String[]> params = request.getParameterMap();
        if (!params.isEmpty()) {

            try {
                String pid = params.get("id")[0];
                long id = Long.valueOf(pid);
                List<UserDataSet> users = new ArrayList<>();
                UserDataSet user = service.load(id, UserDataSet.class);
                if (user ==null) values.put("messageSystem", " users get cannot from DB ");
                else {
                    users.add(user);
                    values.put("users",users);
                    values.put("messageSystem", "users get from DB ");
                }
            } catch (Exception e) {
                e.printStackTrace();
                values.put("messageSystem", "cannot  get users from DB ");
            }

        } else values.put("messageSystem", "");
        String page = templateProcessor.getPage(GET_PAGE_TEMPLATE, values);//save to the page

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(page);
    }

}
