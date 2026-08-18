package ru.otus.java.pro.hw06.cell;

import ru.otus.java.pro.hw06.domain.Denomination;

import java.util.Objects;

public final class CassetteCell implements BanknoteCell {

    private final Denomination denomination;
    private int count;

    public CassetteCell(Denomination denomination) {
        this.denomination = Objects.requireNonNull(denomination, "Номинал ячейки не может быть null");
    }

    @Override
    public Denomination denomination() {
        return denomination;
    }

    @Override
    public void put(int count) {
        requirePositiveCount(count);
        this.count += count;
    }

    @Override
    public void take(int count) {
        requirePositiveCount(count);
        if (count > this.count) {
            throw new IllegalStateException(
                    "Недостаточно банкнот номинала " + denomination.value()
                            + ": запрошено " + count + ", доступно " + this.count);
        }
        this.count -= count;
    }

    @Override
    public int count() {
        return count;
    }

    private static void requirePositiveCount(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Количество банкнот должно быть положительным");
        }
    }
}
