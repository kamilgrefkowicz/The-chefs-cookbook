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
    public static final String CHOOSE_FROM_LIST = "Wybierz produkt z listy. Jeżeli w bazie nie ma szukanego przez Ciebie przedmiotu, możesz go dodać po prawej stronie";

    public static final String USER_NAME_TAKEN = "Użytkownik o takiej nazwie już istnieje";
    public static final String USER_CREATION_SUCCESSFUL = "Twoje konto zostało utworzone";

    public static final String MENU_NAME_TAKEN = "Masz już menu o takiej nazwie";

    public static final String ITEM_DELETED = "Usunięto produkt";
    public static final String BASIC_ITEM_CREATED = "Dodano produkt do bazy";
    public static final String INGREDIENT_ADDED = "Dodano składnik";
    public static final String INGREDIENT_REMOVED = "Usunięto składnik";
    public static final String YIELD_SET = "Ustalono wydajność";
    public static final String DESCRIPTION_MODIFIED = "Zmieniono opis";

    public static final String ITEMS_ADDED = "Dodano przedmioty do menu";
    public static final String ITEMS_REMOVED = "Usunięto przedmioty z menu";



}
