package ru.otus.java.pro.hw12.services;

public interface UserAuthService {

    boolean authenticate(String login, String password);
}
