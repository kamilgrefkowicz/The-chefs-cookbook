package pl.kamil.chefscookbook.food.domain.staticData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

import static pl.kamil.chefscookbook.language.LanguageConfiguration.getLanguage;

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

    public static Unit INTERMEDIATE() {
        return new Unit (2, "półprodukt", "intermediate" );
    }

    public static Unit DISH() {
        return new Unit (3, "danie", "dish" );
    }
}

