package ru.otus.java.pro.hw06.strategy;

import ru.otus.java.pro.hw06.domain.Denomination;

import java.util.Map;
import java.util.Optional;

public interface WithdrawalStrategy {

    Optional<Map<Denomination, Integer>> plan(int amount, Map<Denomination, Integer> available);
}
