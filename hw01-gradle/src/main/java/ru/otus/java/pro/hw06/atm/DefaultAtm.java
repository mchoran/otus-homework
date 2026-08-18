package ru.otus.java.pro.hw06.atm;

import ru.otus.java.pro.hw06.cell.BanknoteCell;
import ru.otus.java.pro.hw06.domain.CashBundle;
import ru.otus.java.pro.hw06.domain.Denomination;
import ru.otus.java.pro.hw06.exception.CannotDispenseException;
import ru.otus.java.pro.hw06.strategy.WithdrawalStrategy;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class DefaultAtm implements Atm {

    private final Map<Denomination, BanknoteCell> cells;
    private final WithdrawalStrategy withdrawalStrategy;

    public DefaultAtm(Collection<BanknoteCell> cells, WithdrawalStrategy withdrawalStrategy) {
        Objects.requireNonNull(cells, "Ячейки не могут быть null");
        this.withdrawalStrategy = Objects.requireNonNull(withdrawalStrategy, "Стратегия выдачи не может быть null");
        if (cells.isEmpty()) {
            throw new IllegalArgumentException("Банкомат должен содержать хотя бы одну ячейку");
        }
        this.cells = toUniqueCells(cells);
    }

    @Override
    public void deposit(Denomination denomination, int count) {
        cellFor(denomination).put(count);
    }

    @Override
    public CashBundle withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма выдачи должна быть положительной");
        }

        Map<Denomination, Integer> plan = withdrawalStrategy
                .plan(amount, availableBanknotes())
                .orElseThrow(() -> new CannotDispenseException(amount));

        plan.forEach((denomination, count) -> cellFor(denomination).take(count));
        return new CashBundle(plan);
    }

    @Override
    public long getBalance() {
        return cells.values().stream()
                .mapToLong(BanknoteCell::balance)
                .sum();
    }

    private BanknoteCell cellFor(Denomination denomination) {
        Objects.requireNonNull(denomination, "Номинал не может быть null");
        BanknoteCell cell = cells.get(denomination);
        if (cell == null) {
            throw new IllegalArgumentException("Банкомат не принимает номинал: " + denomination.value());
        }
        return cell;
    }

    private Map<Denomination, Integer> availableBanknotes() {
        Map<Denomination, Integer> available = new EnumMap<>(Denomination.class);
        cells.forEach((denomination, cell) -> {
            if (cell.count() > 0) {
                available.put(denomination, cell.count());
            }
        });
        return available;
    }

    private static Map<Denomination, BanknoteCell> toUniqueCells(Collection<BanknoteCell> cells) {
        Map<Denomination, BanknoteCell> unique = new EnumMap<>(Denomination.class);
        for (BanknoteCell cell : cells) {
            Objects.requireNonNull(cell, "Ячейка не может быть null");
            BanknoteCell previous = unique.put(cell.denomination(), cell);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Дублируется ячейка номинала: " + cell.denomination().value());
            }
        }
        return unique;
    }
}
