package pl.kamil.chefscookbook.food.domain.staticData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

import static pl.kamil.chefscookbook.configuration.LanguageConfiguration.getLanguage;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Type {

    @Id
    int id;

    String namePl;
    String nameEn;

    @Override
    public String toString() {
        if (getLanguage().equals("pl")) return namePl;
        return nameEn;
    }

    public static Type BASIC() {
        return new Type (1, "podstawowy", "basic" );
    }

    public static Type INTERMEDIATE() {
        return new Type (2, "półprodukt", "intermediate" );
    }

    public static Type DISH() {
        return new Type (3, "danie", "dish" );
    }
}

