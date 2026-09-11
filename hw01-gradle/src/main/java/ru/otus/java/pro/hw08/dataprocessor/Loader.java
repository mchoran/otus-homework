package ru.otus.java.pro.hw08.dataprocessor;

import ru.otus.java.pro.hw08.model.Measurement;

import java.util.List;

public interface Loader {

    List<Measurement> load();
}
