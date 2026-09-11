package ru.otus.java.pro.hw08.dataprocessor;

import ru.otus.java.pro.hw08.model.Measurement;

import java.util.List;
import java.util.Map;

public interface Processor {

    Map<String, Double> process(List<Measurement> data);
}
