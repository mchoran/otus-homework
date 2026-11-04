package ru.otus.java.pro.hw12.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.otus.java.pro.hw10.crm.service.DbServiceClient;
import ru.otus.java.pro.hw12.dto.ClientDto;
import ru.otus.java.pro.hw12.services.TemplateProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";

    private final TemplateProcessor templateProcessor;
    private final DbServiceClient dbServiceClient;

    public ClientsServlet(TemplateProcessor templateProcessor, DbServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        render(request, response, null, HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String phones = request.getParameter("phones");
        ClientDto dto = new ClientDto(
                null,
                request.getParameter("name"),
                request.getParameter("address"),
                phones == null ? java.util.List.of() : Arrays.stream(phones.split(",")).toList());
        try {
            dbServiceClient.saveClient(dto.toClient());
        } catch (IllegalArgumentException exception) {
            render(request, response, exception.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.sendRedirect("/clients");
    }

    private void render(HttpServletRequest request, HttpServletResponse response, String error, int status)
            throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("clients", dbServiceClient.findAll());
        HttpSession session = request.getSession(false);
        if (session != null) {
            data.put("admin", session.getAttribute(AuthorizationFilter.ADMIN_SESSION_ATTRIBUTE));
        }
        if (error != null) {
            data.put("error", error);
        }
        response.setStatus(status);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, data));
    }
}
