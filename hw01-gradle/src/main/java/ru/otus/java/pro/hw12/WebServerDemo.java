package ru.otus.java.pro.hw12;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.hw10.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.java.pro.hw10.crm.dbmigrations.HibernateUtils;
import ru.otus.java.pro.hw10.crm.model.Address;
import ru.otus.java.pro.hw10.crm.model.Client;
import ru.otus.java.pro.hw10.crm.model.Phone;
import ru.otus.java.pro.hw10.crm.repository.DataTemplateHibernate;
import ru.otus.java.pro.hw10.crm.service.DbServiceClient;
import ru.otus.java.pro.hw10.crm.service.DbServiceClientImpl;
import ru.otus.java.pro.hw12.server.ClientsWebServer;
import ru.otus.java.pro.hw12.services.AdminAuthService;
import ru.otus.java.pro.hw12.services.TemplateProcessor;
import ru.otus.java.pro.hw12.services.TemplateProcessorImpl;

/*
Стартовая страница: http://localhost:8080
Страница клиентов:  http://localhost:8080/clients
Логин и пароль:     admin / admin

Перед запуском поднимите PostgreSQL из hw10:
docker compose -f hw01-gradle/docker-compose-hw10.yml up -d
*/
public class WebServerDemo {

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/hw12/templates";
    private static final String URL = "jdbc:postgresql://localhost:5431/hibernateDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(WebServerDemo.class);

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration()
                .setProperty(AvailableSettings.JAKARTA_JDBC_URL, URL)
                .setProperty(AvailableSettings.JAKARTA_JDBC_USER, USER)
                .setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, PASSWORD)
                .setProperty(AvailableSettings.JAKARTA_JDBC_DRIVER, "org.postgresql.Driver")
                .setProperty(AvailableSettings.HBM2DDL_AUTO, "update")
                .setProperty(AvailableSettings.SHOW_SQL, "true")
                .setProperty(AvailableSettings.FORMAT_SQL, "true");

        var sessionFactory = HibernateUtils.buildSessionFactory(
                configuration,
                Client.class,
                Address.class,
                Phone.class);
        Runtime.getRuntime().addShutdownHook(new Thread(sessionFactory::close));

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        DbServiceClient dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        ClientsWebServer webServer = new ClientsWebServer(
                WEB_SERVER_PORT,
                new AdminAuthService(),
                dbServiceClient,
                templateProcessor);

        webServer.start();
        log.info("web server started at http://localhost:{}", webServer.getPort());
        webServer.join();
    }
}
