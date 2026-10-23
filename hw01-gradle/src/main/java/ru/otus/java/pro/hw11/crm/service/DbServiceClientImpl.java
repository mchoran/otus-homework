package ru.otus.java.pro.hw11.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.hw11.cachehw.HwCache;
import ru.otus.java.pro.hw11.core.sessionmanager.TransactionManager;
import ru.otus.java.pro.hw11.crm.model.Client;
import ru.otus.java.pro.hw11.crm.repository.DataTemplate;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DbServiceClient {

    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final TransactionManager transactionManager;
    private final DataTemplate<Client> dataTemplate;
    private final HwCache<Long, Client> cache;

    public DbServiceClientImpl(
            TransactionManager transactionManager,
            DataTemplate<Client> dataTemplate,
            HwCache<Long, Client> cache) {
        this.transactionManager = transactionManager;
        this.dataTemplate = dataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        var savedClient = transactionManager.doInTransaction(session -> {
            if (client.getId() == null) {
                return dataTemplate.insert(session, client);
            }
            return dataTemplate.update(session, client);
        });
        cache.put(savedClient.getId(), savedClient);
        log.info("saved client: {}", savedClient);
        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cachedClient = cache.get(id);
        if (cachedClient != null) {
            return Optional.of(cachedClient);
        }
        return transactionManager.doInTransaction(session -> {
            var client = dataTemplate.findById(session, id);
            client.ifPresent(found -> cache.put(found.getId(), found));
            return client;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInTransaction(dataTemplate::findAll);
    }
}
