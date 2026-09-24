package ru.otus.java.pro.hw09.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.hw09.core.sessionmanager.TransactionRunner;
import ru.otus.java.pro.hw09.crm.model.Client;
import ru.otus.java.pro.hw09.mapper.DataTemplate;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DbServiceClient {

    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final TransactionRunner transactionRunner;
    private final DataTemplate<Client> dataTemplate;

    public DbServiceClientImpl(
            TransactionRunner transactionRunner,
            DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                dataTemplate.insert(connection, client);
            } else {
                dataTemplate.update(connection, client);
            }
            log.info("saved client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionRunner.doInTransaction(
                connection -> dataTemplate.findById(connection, id));
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(dataTemplate::findAll);
    }
}
