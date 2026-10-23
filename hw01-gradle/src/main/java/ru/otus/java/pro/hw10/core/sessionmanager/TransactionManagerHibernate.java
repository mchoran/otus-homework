package ru.otus.java.pro.hw10.core.sessionmanager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.function.Function;

public class TransactionManagerHibernate implements TransactionManager {

    private final SessionFactory sessionFactory;

    public TransactionManagerHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public <T> T doInTransaction(Function<Session, T> action) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            try {
                var result = action.apply(session);
                transaction.commit();
                return result;
            } catch (RuntimeException e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }
}
