package pl.kamil.chefscookbook.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.user.application.port.FillTestUserWithRecipesUseCase;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.DISH;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.INTERMEDIATE;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;

@Component
@RequiredArgsConstructor
public class FillTestUserWithRecipes implements FillTestUserWithRecipesUseCase {

    private final ItemRepository itemRepository;

    @Override
    public void execute(UserEntity user) {

        Map<String, Item> intermediates = generateIntermediates(user);
        List<Long> dishes = generateDishes(user, intermediates);
    }



    private Map<String, Item> generateIntermediates(UserEntity user) {

        Map<String, Item> intermediateIds = new HashMap<>();

        Item shakshukaBase = new Item("Shakshuka baza", KILOGRAM, INTERMEDIATE, user);
        addPublicIngredient(shakshukaBase, "Papryka kolorowa", 0.5);
        addPublicIngredient(shakshukaBase, "Ciecierzyca", 0.4);
        addPublicIngredient(shakshukaBase, "Czosnek obrany", 0.1);
        addPublicIngredient(shakshukaBase, "Pieprz", 0.01);
        addPublicIngredient(shakshukaBase, "Pomidory pelati", 2.5);
        addPublicIngredient(shakshukaBase, "Seler naciowy", 0.5);
        addPublicIngredient(shakshukaBase, "Sól", 0.02);
        addPublicIngredient(shakshukaBase, "Cebula czerwona", 0.4);
        addPublicIngredient(shakshukaBase, "Kmin rzymski", 0.02);
        addPublicIngredient(shakshukaBase, "Oliwa z oliwek", 0.2);
        shakshukaBase.getRecipe().setRecipeYield(BigDecimal.valueOf(3.6));
        shakshukaBase.getRecipe().setDescription("Cebulę, seler, paprykę pokrój w kawałki 2x2cm, czosnek drobno. Podsmaż na oliwie. Dodaj pelati, doprowadź do wrzenia i gotuj mieszając przez 20 minut. Dodaj ciecierzycę i dopraw do smaku. Gotuj kolejne 10 minut");
        saveAndStore(shakshukaBase, intermediateIds);

        Item cottageCheeseSpread = new Item("Pasta z twarożkiem", KILOGRAM, INTERMEDIATE, user);
        addPublicIngredient(cottageCheeseSpread, "Twaróg półtłusty", 1);
        addPublicIngredient(cottageCheeseSpread, "Szczypiorek", 0.1);
        addPublicIngredient(cottageCheeseSpread, "Śmietanka 34%", 0.25);
        addPublicIngredient(cottageCheeseSpread, "Sól", 0);
        addPublicIngredient(cottageCheeseSpread, "Pieprz", 0);
        cottageCheeseSpread.getRecipe().setDescription("Szczypiorek drobno posiekaj. Wymieszaj wszystkie składniki do uzyskania lekko luźnej konsystencji");
        saveAndStore(cottageCheeseSpread, intermediateIds);

        Item eggSpread = new Item("Pasta jajeczna", KILOGRAM, INTERMEDIATE, user);
        addPublicIngredient(eggSpread, "Jajko", 12);
        addPublicIngredient(eggSpread, "Majonez", 0.2);
        addPublicIngredient(eggSpread, "Sól", 0);
        addPublicIngredient(eggSpread, "Pieprz", 0);
        eggSpread.getRecipe().setDescription("Jajka ugotuj na twardo (~6 minut w lekko wrzącej wodzie), wystudź, obierz, zetrzyj na tarce. Dodaj majonez, wymieszaj, dopraw");
        eggSpread.getRecipe().setRecipeYield(BigDecimal.valueOf(0.8));
        saveAndStore(eggSpread, intermediateIds);

        Item tunaSpread = new Item("Pasta z tuńczyka", KILOGRAM, INTERMEDIATE, user);
        addPublicIngredient(tunaSpread, "Tuńczyk w zalewie", 1);
        addPublicIngredient(tunaSpread, "Majonez", 0.2);
        addPublicIngredient(tunaSpread, "Sól", 0);
        addPublicIngredient(tunaSpread, "Pieprz", 0);
        tunaSpread.getRecipe().setDescription("Tuńczyka porządnie odcisnąć z zalewy. Dodać majonez, wymieszać, doprawić");
        tunaSpread.getRecipe().setRecipeYield(BigDecimal.valueOf(0.8));
        saveAndStore(tunaSpread, intermediateIds);

        Item chilliJam = new Item("Dżem z chilli", KILOGRAM, INTERMEDIATE, user);
        addPublicIngredient(chilliJam, "Chilli", 0.1);
        addPublicIngredient(chilliJam, "Cukier", 0.5);
        addPublicIngredient(chilliJam, "Imbir", 0.06);
        addPublicIngredient(chilliJam, "Sos sojowy", 0.06);
        addPublicIngredient(chilliJam, "Czosnek obrany", 0.1);
        addPublicIngredient(chilliJam, "Ocet winny", 0.15);
        addPublicIngredient(chilliJam, "Papryka czerwona", 0.3);
        chilliJam.getRecipe().setRecipeYield(BigDecimal.valueOf(0.4));
        chilliJam.getRecipe().setDescription("Czosnek, paprykę i chilli zmiel malakserem (jeśli chcesz użyć blendera, to najpierw usuń wszystkie pestki a później dodaj je z powrotem po zblendowaniu). Wrzuć do garnka z resztą składników. Gotuj na małym ogniu do uzyskania żelowej konsystencji");
        saveAndStore(chilliJam, intermediateIds);

        return intermediateIds;
    }
    private List<Long> generateDishes(UserEntity user, Map<String, Item> intermediates) {
        List<Long> dishIds = new ArrayList<>();

        Item cottageRoll = new Item("Bułka z twarożkiem", null, DISH, user);
        addPublicIngredient(cottageRoll, "Bułka maślana", 1);
        addPublicIngredient(cottageRoll, "Rzodkiewka", 1);
        addPublicIngredient(cottageRoll, "Ogórek świeży", 0.01);
        addPublicIngredient(cottageRoll, "Sałata rzymska", 0.01);
        cottageRoll.addIngredient(intermediates.get("Pasta z twarożkiem"), BigDecimal.valueOf(0.05));
        cottageRoll.getRecipe().setDescription("Bułkę rozkrój i podpiecz. Posmaruj pastą, dodaj posiekane warzywa. Wydawaj na małym talerzu.");
        saveAndStore(cottageRoll, dishIds);


        return dishIds;
    }

    private void saveAndStore( Item cottageRoll, List<Long> dishIds) {
        cottageRoll = itemRepository.save(cottageRoll);
        dishIds.add(cottageRoll.getId());
    }

    private void saveAndStore(Item chilliJam, Map<String, Item> intermediateIds ) {
        chilliJam = itemRepository.save(chilliJam);
        intermediateIds.put(chilliJam.getName(), chilliJam);
    }


    private void addPublicIngredient(Item item, String s, double amount) {
        item.addIngredient(itemRepository.findPublicItemByName(s), BigDecimal.valueOf(amount));
    }
}
