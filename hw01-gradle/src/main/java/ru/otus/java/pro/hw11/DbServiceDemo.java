package ru.otus.java.pro.hw11;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.hw11.cachehw.MyCache;
import ru.otus.java.pro.hw11.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.java.pro.hw11.crm.dbmigrations.HibernateUtils;
import ru.otus.java.pro.hw11.crm.model.Address;
import ru.otus.java.pro.hw11.crm.model.Client;
import ru.otus.java.pro.hw11.crm.model.Phone;
import ru.otus.java.pro.hw11.crm.repository.DataTemplateHibernate;
import ru.otus.java.pro.hw11.crm.service.DbServiceClientImpl;

import java.util.List;

public class DbServiceDemo {

    private static final int READS = 1_000;
    private static final String URL = "jdbc:postgresql://localhost:5433/cacheDB";
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
                .setProperty(AvailableSettings.SHOW_SQL, "false");

        try (var sessionFactory = HibernateUtils.buildSessionFactory(
                configuration,
                Client.class,
                Address.class,
                Phone.class)) {
            var transactionManager = new TransactionManagerHibernate(sessionFactory);
            var clientTemplate = new DataTemplateHibernate<>(Client.class);
            var cache = new MyCache<Long, Client>();
            var dbServiceClient = new DbServiceClientImpl(
                    transactionManager, clientTemplate, cache);

            var savedClient = dbServiceClient.saveClient(new Client(
                    "Cached client",
                    new Address("Lenina street, 1"),
                    List.of(new Phone("+7-900-111-22-33"))));
            long clientId = savedClient.getId();

            var loadedClient = dbServiceClient.getClient(clientId)
                    .orElseThrow(() -> new IllegalStateException("Client was not saved"));
            log.info("loaded from cache: {}", loadedClient);

            dbServiceClient.getClient(clientId);
            long cacheTime = measure(() -> {
                for (int i = 0; i < READS; i++) {
                    dbServiceClient.getClient(clientId).orElseThrow();
                }
            });
            long databaseTime = measure(() -> {
                for (int i = 0; i < READS; i++) {
                    transactionManager.doInTransaction(session ->
                            clientTemplate.findById(session, clientId).orElseThrow());
                }
            });

            log.info("{} cached reads: {} us", READS, cacheTime);
            log.info("{} database reads: {} us", READS, databaseTime);
        }
    }

    private static long measure(Runnable action) {
        long startedAt = System.nanoTime();
        action.run();
        return (System.nanoTime() - startedAt) / 1_000;
    }
}
