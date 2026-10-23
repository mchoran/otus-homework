package ru.otus.java.pro.hw09.mapper;

public interface EntitySQLMetaData {

    String getSelectAllSql();

    String getSelectByIdSql();

    String getInsertSql();

    String getUpdateSql();
}
