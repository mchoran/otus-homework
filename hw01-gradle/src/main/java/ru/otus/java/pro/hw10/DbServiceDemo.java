package ru.otus.java.pro.hw10;

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
import ru.otus.java.pro.hw10.crm.service.DbServiceClientImpl;

import java.util.List;

public class DbServiceDemo {

    private static final String URL = "jdbc:postgresql://localhost:5431/hibernateDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) {
        var configuration = new Configuration()
                .setProperty(AvailableSettings.JAKARTA_JDBC_URL, URL)
                .setProperty(AvailableSettings.JAKARTA_JDBC_USER, USER)
                .setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, PASSWORD)
                .setProperty(AvailableSettings.JAKARTA_JDBC_DRIVER, "org.postgresql.Driver")
                .setProperty(AvailableSettings.HBM2DDL_AUTO, "create")
                .setProperty(AvailableSettings.SHOW_SQL, "true")
                .setProperty(AvailableSettings.FORMAT_SQL, "true")
                .setProperty(AvailableSettings.HIGHLIGHT_SQL, "false")
                .setProperty(AvailableSettings.USE_SQL_COMMENTS, "true");

        try (var sessionFactory = HibernateUtils.buildSessionFactory(
                configuration,
                Client.class,
                Address.class,
                Phone.class)) {
            var transactionManager = new TransactionManagerHibernate(sessionFactory);
            var clientTemplate = new DataTemplateHibernate<>(Client.class);
            var dbServiceClient =
                    new DbServiceClientImpl(transactionManager, clientTemplate);

            var client = new Client(
                    "Client with contacts",
                    new Address("Lenina street, 1"),
                    List.of(
                            new Phone("+7-900-111-22-33"),
                            new Phone("+7-900-444-55-66")));

            var savedClient = dbServiceClient.saveClient(client);
            var loadedClient = dbServiceClient
                    .getClient(savedClient.getId())
                    .orElseThrow(() -> new IllegalStateException(
                            "Client not found, id: " + savedClient.getId()));

            log.info("loaded client: {}", loadedClient);
            log.info("loaded address: {}", loadedClient.getAddress());
            log.info("loaded phones: {}", loadedClient.getPhones());

            var tables = transactionManager.doInTransaction(session ->
                    session.createNativeQuery(
                                    """
                                    select table_name
                                    from information_schema.tables
                                    where table_schema = 'public'
                                    order by table_name
                                    """,
                                    String.class)
                            .getResultList());
            log.info("tables created by Hibernate: {}", tables);
        }
    }
}
