package pl.kamil.chefscookbook.food.domain.staticData;

public enum Unit {
    KILOGRAM("kg"),
    LITRE("l"),
    PIECE("szt");

    String namePl;

    Unit(String namePl) {
        this.namePl = namePl;
    }

    @Override
    public String toString() {
        return namePl;
    }
}
