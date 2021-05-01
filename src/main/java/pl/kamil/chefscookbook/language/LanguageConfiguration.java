package pl.kamil.chefscookbook.language;

import java.util.Locale;

public class LanguageConfiguration {

    public static String getLanguage(){
        return Locale.getDefault().getLanguage();
    }
}
