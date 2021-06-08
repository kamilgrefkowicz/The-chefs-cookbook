package pl.kamil.chefscookbook.food.domain.staticData;

import java.util.StringJoiner;

public enum Unit {
    KILOGRAM("KG"),
    LITRE("LT"),
    PIECE("SZT");

    String namePl;

    Unit(String namePl) {
        this.namePl = namePl;
    }

    @Override
    public String toString() {
        return namePl;
    }
}
