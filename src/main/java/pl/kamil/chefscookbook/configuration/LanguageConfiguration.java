package pl.kamil.chefscookbook.configuration;

import java.util.Locale;

public class LanguageConfiguration {

    private LanguageConfiguration() {
        throw new IllegalStateException("Utility class");
    }

    public static String getLanguage(){
        return Locale.getDefault().getLanguage();
    }

}
