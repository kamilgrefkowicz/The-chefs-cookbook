package pl.kamil.chefscookbook.food.domain.staticData;


public enum Type {
    BASIC("podstawowy"),
    INTERMEDIATE ("półprodukt"),
    DISH ("danie");

    String namePl;

    Type(String namePl) {
        this.namePl = namePl;
    }

    @Override
    public String toString() {
        return namePl;
    }
}
