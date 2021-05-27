package pl.kamil.chefscookbook.food.domain.staticData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.List;

import static pl.kamil.chefscookbook.shared.language.LanguageConfiguration.getLanguage;

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

    public static Unit getUnitFromId(int id) {
        switch (id) {
            case 1:
                return KILOGRAM();
            case 2:
                return LITRE();
            case 3:
                return PIECE();
            default: throw new IllegalArgumentException();
        }
    }
    public static List<Unit> unitList() {
        return List.of(KILOGRAM(), LITRE(), PIECE());
    }
}
