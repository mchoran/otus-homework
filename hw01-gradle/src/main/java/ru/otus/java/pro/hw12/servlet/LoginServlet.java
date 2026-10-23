package ru.otus.java.pro.hw12.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.otus.java.pro.hw12.services.TemplateProcessor;
import ru.otus.java.pro.hw12.services.UserAuthService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {

    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final int MAX_INACTIVE_INTERVAL_SECONDS = 30 * 60;

    private final TemplateProcessor templateProcessor;
    private final UserAuthService userAuthService;

    public LoginServlet(TemplateProcessor templateProcessor, UserAuthService userAuthService) {
        this.templateProcessor = templateProcessor;
        this.userAuthService = userAuthService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        render(response, null, HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        if (!userAuthService.authenticate(login, password)) {
            render(response, "Неверный логин или пароль", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute(AuthorizationFilter.ADMIN_SESSION_ATTRIBUTE, login);
        session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL_SECONDS);
        response.sendRedirect("/clients");
    }

    private void render(HttpServletResponse response, String error, int status) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (error != null) {
            data.put("error", error);
        }
        response.setStatus(status);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, data));
    }
}
