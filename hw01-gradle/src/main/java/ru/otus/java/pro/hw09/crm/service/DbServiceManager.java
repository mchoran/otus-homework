package ru.otus.java.pro.hw09.crm.service;

import ru.otus.java.pro.hw09.crm.model.Manager;

import java.util.List;
import java.util.Optional;

public interface DbServiceManager {

    Manager saveManager(Manager manager);

    Optional<Manager> getManager(long no);

    List<Manager> findAll();
}
