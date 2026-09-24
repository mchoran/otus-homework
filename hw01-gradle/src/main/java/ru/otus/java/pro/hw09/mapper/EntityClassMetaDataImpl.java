package ru.otus.java.pro.hw09.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> entityClass;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.allFields = List.copyOf(Arrays.asList(entityClass.getDeclaredFields()));
        this.idField = findIdField(allFields);
        this.fieldsWithoutId = allFields.stream()
                .filter(field -> !field.equals(idField))
                .toList();
        this.constructor = findConstructor(entityClass, allFields);

        allFields.forEach(field -> field.setAccessible(true));
        constructor.setAccessible(true);
    }

    @Override
    public String getName() {
        return entityClass.getSimpleName().toLowerCase(Locale.ROOT);
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

    private static Field findIdField(List<Field> fields) {
        var idFields = fields.stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .toList();
        if (idFields.size() != 1) {
            throw new DataTemplateException(
                    "Entity must contain exactly one field annotated with @Id");
        }
        return idFields.getFirst();
    }

    private static <T> Constructor<T> findConstructor(Class<T> entityClass, List<Field> fields) {
        var parameterTypes = fields.stream()
                .map(Field::getType)
                .toArray(Class<?>[]::new);
        try {
            return entityClass.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new DataTemplateException(
                    "Entity must have a constructor with all fields in declaration order: "
                            + entityClass.getName());
        }
    }
}
