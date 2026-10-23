package ru.otus.java.pro.hw10.crm.dbmigrations;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtils {

    private HibernateUtils() {
    }

    public static SessionFactory buildSessionFactory(
            Configuration configuration,
            Class<?>... annotatedClasses) {
        for (Class<?> annotatedClass : annotatedClasses) {
            configuration.addAnnotatedClass(annotatedClass);
        }
        return configuration.buildSessionFactory();
    }
}
