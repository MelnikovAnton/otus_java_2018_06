package ru.otus.hw15.frontendService.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.hw15.dbService.service.DBService;
import ru.otus.hw15.frontendService.TemplateProcessor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    private static final String GET_PAGE_TEMPLATE = "login.html";

    @Autowired
    private TemplateProcessor templateProcessor;
    @Autowired
    private DBService service;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }


    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {

        Map<String, String[]> params = request.getParameterMap();
        if (!params.isEmpty()) {
            String user = params.get("user")[0];
            System.out.println(user);

        }
        String page = templateProcessor.getPage(GET_PAGE_TEMPLATE, null);//save to the page

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(page);

    }

}
