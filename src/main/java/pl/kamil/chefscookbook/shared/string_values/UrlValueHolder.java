package pl.kamil.chefscookbook.shared.string_values;


public class UrlValueHolder {
    private UrlValueHolder() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ERROR = "error";

    public static final String MENU_CREATE = "menu/new-menu";
    public static final String MENU_LIST = "menu/my-menus";
    public static final String MENU_VIEW = "menu/view-menu";
    public static final String MENU_ADD_ITEMS = "menu/add-items";
    public static final String MENU_DELETE_CONFIRM = "menu/delete-menu";


    public static final String ITEM_MODIFY = "food/modify-items/modify-item";
    public static final String ITEM_CREATE = "food/modify-items/create-item";
    public static final String ITEM_DELETE_CONFIRM = "food/modify-items/delete-confirm";
    public static final String ITEMS_LIST = "food/my-items";
    public static final String ITEM_VIEW = "food/view-item";
    public static final String BASIC_ITEMS_VIEW = "food/view-basics";

    public static final String USER_NEW = "user/registration";


}
