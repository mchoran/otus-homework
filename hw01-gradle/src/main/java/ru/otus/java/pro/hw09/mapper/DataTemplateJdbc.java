package ru.otus.java.pro.hw09.mapper;

import ru.otus.java.pro.hw09.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor,
            EntitySQLMetaData entitySQLMetaData,
            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
                        connection,
                        entitySQLMetaData.getSelectByIdSql(),
                        List.of(id),
                        this::createEntity)
                .stream()
                .findFirst();
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectAllSql(),
                List.of(),
                this::createEntity);
    }

    @Override
    public void insert(Connection connection, T object) {
        var generatedId = dbExecutor.executeStatement(
                connection,
                entitySQLMetaData.getInsertSql(),
                getFieldValues(object, entityClassMetaData.getFieldsWithoutId()));
        setGeneratedId(object, generatedId);
    }

    @Override
    public void update(Connection connection, T object) {
        var parameters = new ArrayList<>(
                getFieldValues(object, entityClassMetaData.getFieldsWithoutId()));
        parameters.add(getFieldValue(object, entityClassMetaData.getIdField()));
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), parameters);
    }

    private T createEntity(ResultSet resultSet) throws SQLException {
        var arguments = new Object[entityClassMetaData.getAllFields().size()];
        for (int index = 0; index < arguments.length; index++) {
            var field = entityClassMetaData.getAllFields().get(index);
            arguments[index] = resultSet.getObject(field.getName(), field.getType());
        }
        try {
            return entityClassMetaData.getConstructor().newInstance(arguments);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DataTemplateException(e);
        }
    }

    private static List<Object> getFieldValues(Object object, List<Field> fields) {
        return fields.stream()
                .map(field -> getFieldValue(object, field))
                .toList();
    }

    private static Object getFieldValue(Object object, Field field) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }

    private void setGeneratedId(T object, long generatedId) {
        var idField = entityClassMetaData.getIdField();
        try {
            if (idField.getType() == Long.class || idField.getType() == long.class) {
                idField.set(object, generatedId);
            } else if (idField.getType() == Integer.class || idField.getType() == int.class) {
                idField.set(object, Math.toIntExact(generatedId));
            } else {
                throw new DataTemplateException(
                        "Unsupported @Id type: " + idField.getType().getName());
            }
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }
}
