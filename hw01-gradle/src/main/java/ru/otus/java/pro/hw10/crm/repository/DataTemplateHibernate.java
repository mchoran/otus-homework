package ru.otus.java.pro.hw10.crm.repository;

import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class DataTemplateHibernate<T> implements DataTemplate<T> {

    private final Class<T> entityClass;

    public DataTemplateHibernate(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Optional<T> findById(Session session, long id) {
        return Optional.ofNullable(session.find(entityClass, id));
    }

    @Override
    public List<T> findAll(Session session) {
        return session.createQuery(
                        "from " + entityClass.getSimpleName(),
                        entityClass)
                .getResultList();
    }

    @Override
    public T insert(Session session, T object) {
        session.persist(object);
        return object;
    }

    @Override
    public T update(Session session, T object) {
        return session.merge(object);
    }
}
