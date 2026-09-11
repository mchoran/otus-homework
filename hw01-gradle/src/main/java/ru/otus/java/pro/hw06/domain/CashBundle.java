package ru.otus.java.pro.hw06.domain;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class CashBundle {

    private final Map<Denomination, Integer> banknotes;

    public CashBundle(Map<Denomination, Integer> banknotes) {
        Objects.requireNonNull(banknotes, "Набор банкнот не может быть null");
        EnumMap<Denomination, Integer> copy = new EnumMap<>(Denomination.class);
        banknotes.forEach((denomination, count) -> {
            Objects.requireNonNull(denomination, "Номинал не может быть null");
            if (count == null || count <= 0) {
                throw new IllegalArgumentException("Количество банкнот должно быть положительным");
            }
            copy.put(denomination, count);
        });
        this.banknotes = Collections.unmodifiableMap(copy);
    }

    public static CashBundle of(Denomination denomination, int count) {
        return new CashBundle(Map.of(denomination, count));
    }

    public Map<Denomination, Integer> banknotes() {
        return banknotes;
    }

    public int countOf(Denomination denomination) {
        return banknotes.getOrDefault(denomination, 0);
    }

    public int totalCount() {
        return banknotes.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public long totalAmount() {
        return banknotes.entrySet().stream()
                .mapToLong(entry -> (long) entry.getKey().value() * entry.getValue())
                .sum();
    }

    @Override
    public String toString() {
        return "CashBundle" + banknotes;
    }
}
