package ru.otus.java.pro.hw10.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.hw10.core.sessionmanager.TransactionManager;
import ru.otus.java.pro.hw10.crm.model.Client;
import ru.otus.java.pro.hw10.crm.repository.DataTemplate;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DbServiceClient {

    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final TransactionManager transactionManager;
    private final DataTemplate<Client> dataTemplate;

    public DbServiceClientImpl(
            TransactionManager transactionManager,
            DataTemplate<Client> dataTemplate) {
        this.transactionManager = transactionManager;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        var savedClient = transactionManager.doInTransaction(session -> {
            if (client.getId() == null) {
                return dataTemplate.insert(session, client);
            }
            return dataTemplate.update(session, client);
        });
        log.info("saved client: {}", savedClient);
        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionManager.doInTransaction(
                session -> dataTemplate.findById(session, id));
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInTransaction(dataTemplate::findAll);
    }
}
