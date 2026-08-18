package ru.otus.java.pro.hw06.atm;

import ru.otus.java.pro.hw06.cell.BanknoteCell;
import ru.otus.java.pro.hw06.cell.CassetteCell;
import ru.otus.java.pro.hw06.domain.Denomination;
import ru.otus.java.pro.hw06.strategy.GreedyWithdrawalStrategy;
import ru.otus.java.pro.hw06.strategy.WithdrawalStrategy;

import java.util.Arrays;
import java.util.List;

public final class AtmFactory {

    private AtmFactory() {
    }

    public static Atm createDefault() {
        return create(new GreedyWithdrawalStrategy());
    }

    public static Atm create(WithdrawalStrategy strategy) {
        List<BanknoteCell> cells = Arrays.stream(Denomination.values())
                .<BanknoteCell>map(CassetteCell::new)
                .toList();
        return new DefaultAtm(cells, strategy);
    }
}
