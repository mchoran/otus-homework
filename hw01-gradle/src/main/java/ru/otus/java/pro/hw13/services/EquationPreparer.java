package ru.otus.java.pro.hw13.services;

import ru.otus.java.pro.hw13.model.Equation;

import java.util.List;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
