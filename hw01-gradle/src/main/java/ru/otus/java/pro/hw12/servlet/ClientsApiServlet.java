package ru.otus.java.pro.hw12.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.java.pro.hw10.crm.model.Client;
import ru.otus.java.pro.hw10.crm.service.DbServiceClient;
import ru.otus.java.pro.hw12.dto.ClientDto;

import java.io.IOException;
import java.util.Map;

public class ClientsApiServlet extends HttpServlet {

    private final DbServiceClient dbServiceClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ClientsApiServlet(DbServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var clients = dbServiceClient.findAll().stream()
                .map(ClientDto::from)
                .toList();
        writeJson(response, HttpServletResponse.SC_OK, clients);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ClientDto dto;
        try {
            dto = objectMapper.readValue(request.getInputStream(), ClientDto.class);
        } catch (IOException exception) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, Map.of("error", "Некорректный JSON"));
            return;
        }
        if (dto == null) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, Map.of("error", "Некорректный JSON"));
            return;
        }
        try {
            Client saved = dbServiceClient.saveClient(dto.toClient());
            writeJson(response, HttpServletResponse.SC_CREATED, ClientDto.from(saved));
        } catch (IllegalArgumentException exception) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, Map.of("error", exception.getMessage()));
        }
    }

    private void writeJson(HttpServletResponse response, int status, Object body) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
