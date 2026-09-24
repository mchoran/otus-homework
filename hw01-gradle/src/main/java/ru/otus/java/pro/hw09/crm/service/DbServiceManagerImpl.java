package ru.otus.java.pro.hw09.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.hw09.core.sessionmanager.TransactionRunner;
import ru.otus.java.pro.hw09.crm.model.Manager;
import ru.otus.java.pro.hw09.mapper.DataTemplate;

import java.util.List;
import java.util.Optional;

public class DbServiceManagerImpl implements DbServiceManager {

    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final TransactionRunner transactionRunner;
    private final DataTemplate<Manager> dataTemplate;

    public DbServiceManagerImpl(
            TransactionRunner transactionRunner,
            DataTemplate<Manager> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                dataTemplate.insert(connection, manager);
            } else {
                dataTemplate.update(connection, manager);
            }
            log.info("saved manager: {}", manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        return transactionRunner.doInTransaction(
                connection -> dataTemplate.findById(connection, no));
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(dataTemplate::findAll);
    }
}
