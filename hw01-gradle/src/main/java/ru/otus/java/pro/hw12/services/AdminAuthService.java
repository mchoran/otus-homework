package ru.otus.java.pro.hw12.services;

public class AdminAuthService implements UserAuthService {

    public static final String LOGIN = "admin";
    public static final String PASSWORD = "admin";

    @Override
    public boolean authenticate(String login, String password) {
        return LOGIN.equals(login) && PASSWORD.equals(password);
    }
}
