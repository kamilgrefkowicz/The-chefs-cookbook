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
public class Unit {

    @Id
    int id;
    String namePl;
    String nameEn;

    @Override
    public String toString() {
        if (getLanguage().equals("pl")) return namePl;
        return nameEn;
    }

    public static Unit KILOGRAM() {
        return new Unit (1, "KG", "KG" );
    }

    public static Unit LITRE() {
        return new Unit (2, "LT", "LT" );
    }

    public static Unit PIECE() {
        return new Unit (3, "SZT", "PC" );
    }
}
