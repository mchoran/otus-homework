package ru.otus.java.pro.hw09.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSql;
    private final String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        var tableName = entityClassMetaData.getName();
        var idName = entityClassMetaData.getIdField().getName();
        var allColumns = joinFieldNames(entityClassMetaData.getAllFields());
        var columnsWithoutId = joinFieldNames(entityClassMetaData.getFieldsWithoutId());
        var insertParameters = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));
        var updateParameters = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", "));

        selectAllSql = "select " + allColumns + " from " + tableName;
        selectByIdSql = selectAllSql + " where " + idName + " = ?";
        insertSql = "insert into " + tableName + " (" + columnsWithoutId + ") values ("
                + insertParameters + ")";
        updateSql = "update " + tableName + " set " + updateParameters + " where "
                + idName + " = ?";
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }

    private static String joinFieldNames(Iterable<Field> fields) {
        var result = new StringBuilder();
        for (Field field : fields) {
            if (!result.isEmpty()) {
                result.append(", ");
            }
            result.append(field.getName());
        }
        return result.toString();
    }
}
