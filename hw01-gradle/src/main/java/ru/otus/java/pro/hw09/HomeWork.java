package ru.otus.java.pro.hw09;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.hw09.core.repository.executor.DbExecutorImpl;
import ru.otus.java.pro.hw09.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.java.pro.hw09.crm.datasource.DriverManagerDataSource;
import ru.otus.java.pro.hw09.crm.model.Client;
import ru.otus.java.pro.hw09.crm.model.Manager;
import ru.otus.java.pro.hw09.crm.service.DbServiceClientImpl;
import ru.otus.java.pro.hw09.crm.service.DbServiceManagerImpl;
import ru.otus.java.pro.hw09.mapper.DataTemplateJdbc;
import ru.otus.java.pro.hw09.mapper.EntityClassMetaData;
import ru.otus.java.pro.hw09.mapper.EntityClassMetaDataImpl;
import ru.otus.java.pro.hw09.mapper.EntitySQLMetaData;
import ru.otus.java.pro.hw09.mapper.EntitySQLMetaDataImpl;

import javax.sql.DataSource;

@SuppressWarnings({"java:S125", "java:S1481"})
public class HomeWork {

    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityClassMetaData<Client> entityClassMetaDataClient =
                new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient =
                new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(
                dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient);

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        EntityClassMetaData<Manager> entityClassMetaDataManager =
                new EntityClassMetaDataImpl<>(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager =
                new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<>(
                dbExecutor, entitySQLMetaDataManager, entityClassMetaDataManager);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
        dbServiceManager.saveManager(new Manager("ManagerFirst"));

        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));
        var managerSecondSelected = dbServiceManager
                .getManager(managerSecond.getNo())
                .orElseThrow(() -> new RuntimeException(
                        "Manager not found, id:" + managerSecond.getNo()));
        log.info("managerSecondSelected:{}", managerSecondSelected);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/hw09/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
