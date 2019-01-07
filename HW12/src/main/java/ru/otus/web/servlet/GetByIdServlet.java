package ru.otus.web.servlet;

import ru.otus.web.exceptions.MyDBException;
import ru.otus.web.models.UserDataSet;
import ru.otus.web.service.DBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetByIdServlet extends HttpServlet {
    private static final String GET_PAGE_TEMPLATE = "getById.html";


    private final TemplateProcessor templateProcessor;
    private final DBService service;


    public GetByIdServlet(TemplateProcessor templateProcessor, DBService service) {
        this.templateProcessor = templateProcessor;
        this.service=service;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        Map<String,Object> values=new HashMap<>();

        Map<String, String[]> params = request.getParameterMap();
        if (!params.isEmpty()) {

            try {
                String pid = params.get("id")[0];
                long id =Long.valueOf(pid);
                List<UserDataSet> users = service.load(id, UserDataSet.class);
                if (users.isEmpty()) values.put("message"," users get cannot from DB ");
                else {
                    values.put("users", users);
                    values.put("message", "users get from DB ");
                }
            } catch (Exception e) {
                e.printStackTrace();
                values.put("message","cannot  get users from DB ");
            }

        } else values.put("message","");
        String page = templateProcessor.getPage(GET_PAGE_TEMPLATE, values);//save to the page

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(page);
    }

}
