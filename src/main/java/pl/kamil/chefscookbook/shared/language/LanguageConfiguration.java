package pl.kamil.chefscookbook.shared.language;

import java.util.Locale;

public class LanguageConfiguration {

    private LanguageConfiguration() {
        throw new IllegalStateException("Utility class");
    }

    public static String getLanguage(){
        return Locale.getDefault().getLanguage();
    }

}
