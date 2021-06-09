package pl.kamil.chefscookbook.shared.string_values;

public class MessageValueHolder {
    private MessageValueHolder() {
        throw new IllegalStateException("Utility class");
    }

    public static final String NOT_FOUND = "Nie znaleziono szukanego przedmiotu";
    public static final String NOT_AUTHORIZED = "Nie masz uprawnień do tego przedmiotu";
    public static final String LOOP_LONG = " jest już składnikiem ";
    public static final String LOOP_SHORT = "Przedmiot nie może zależeć sam od siebie";
    public static final String ITEM_NAME_TAKEN = "Masz już przedmiot o takiej nazwie";
    public static final String USER_NAME_TAKEN = "Użytkownik o takiej nazwie już istnieje";

}
