package ru.otus.java.pro.hw06.strategy;

import ru.otus.java.pro.hw06.domain.Denomination;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class GreedyWithdrawalStrategy implements WithdrawalStrategy {

    @Override
    public Optional<Map<Denomination, Integer>> plan(int amount, Map<Denomination, Integer> available) {
        Objects.requireNonNull(available, "Доступные банкноты не могут быть null");
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма выдачи должна быть положительной");
        }

        Map<Denomination, Integer> plan = new EnumMap<>(Denomination.class);
        int remaining = amount;

        for (Denomination denomination : available.keySet().stream()
                .sorted(Comparator.comparingInt(Denomination::value).reversed())
                .toList()) {
            int availableCount = available.getOrDefault(denomination, 0);
            int take = Math.min(remaining / denomination.value(), availableCount);
            if (take > 0) {
                plan.put(denomination, take);
                remaining -= take * denomination.value();
            }
        }

        if (remaining != 0) {
            return Optional.empty();
        }
        return Optional.of(plan);
    }
}
