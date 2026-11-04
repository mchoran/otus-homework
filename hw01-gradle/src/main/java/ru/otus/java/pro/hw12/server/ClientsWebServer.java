package ru.otus.java.pro.hw12.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.java.pro.hw10.crm.service.DbServiceClient;
import ru.otus.java.pro.hw12.helpers.FileSystemHelper;
import ru.otus.java.pro.hw12.services.TemplateProcessor;
import ru.otus.java.pro.hw12.services.UserAuthService;
import ru.otus.java.pro.hw12.servlet.AuthorizationFilter;
import ru.otus.java.pro.hw12.servlet.CharacterEncodingFilter;
import ru.otus.java.pro.hw12.servlet.ClientsApiServlet;
import ru.otus.java.pro.hw12.servlet.ClientsServlet;
import ru.otus.java.pro.hw12.servlet.LoginServlet;
import ru.otus.java.pro.hw12.servlet.LogoutServlet;

import java.util.EnumSet;

import static jakarta.servlet.DispatcherType.REQUEST;

public class ClientsWebServer {

    private static final String START_PAGE_NAME = "index.html";
    private static final String STATIC_RESOURCES = "hw12/static";

    private final Server server;

    public ClientsWebServer(
            int port,
            UserAuthService userAuthService,
            DbServiceClient dbServiceClient,
            TemplateProcessor templateProcessor) {
        server = new Server(port);
        server.setHandler(buildHandlers(userAuthService, dbServiceClient, templateProcessor));
        server.setStopAtShutdown(true);
    }

    public void start() throws Exception {
        server.start();
    }

    public void join() throws Exception {
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public int getPort() {
        return ((ServerConnector) server.getConnectors()[0]).getLocalPort();
    }

    private Handler buildHandlers(
            UserAuthService userAuthService,
            DbServiceClient dbServiceClient,
            TemplateProcessor templateProcessor) {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setBaseResource(FileSystemHelper.classpathResource(STATIC_RESOURCES));
        context.setWelcomeFiles(new String[]{START_PAGE_NAME});

        ServletHolder homePage = new ServletHolder("default", DefaultServlet.class);
        homePage.setInitParameter("dirAllowed", "false");
        homePage.setInitParameter("redirectWelcome", "false");
        context.addServlet(homePage, "/");

        context.addFilter(
                new FilterHolder(new CharacterEncodingFilter()),
                "/*",
                EnumSet.of(REQUEST));
        context.addServlet(new ServletHolder(new LoginServlet(templateProcessor, userAuthService)), "/login");
        context.addServlet(new ServletHolder(new LogoutServlet()), "/logout");
        context.addServlet(new ServletHolder(new ClientsServlet(templateProcessor, dbServiceClient)), "/clients");
        context.addServlet(new ServletHolder(new ClientsApiServlet(dbServiceClient)), "/api/clients");

        FilterHolder authorization = new FilterHolder(new AuthorizationFilter());
        context.addFilter(authorization, "/clients", EnumSet.of(REQUEST));
        context.addFilter(authorization, "/api/clients", EnumSet.of(REQUEST));
        return context;
    }
}
