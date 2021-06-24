package pl.kamil.chefscookbook.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.user.application.port.FillTestUserWithRecipesUseCase;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.math.BigDecimal;
import java.util.*;

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

        Map<String, Item> intermediates = new HashMap<>();

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
        saveAndStore(shakshukaBase, intermediates);

        Item cottageCheeseSpread = new Item("Pasta z twarożkiem", KILOGRAM, INTERMEDIATE, user);
        addPublicIngredient(cottageCheeseSpread, "Twaróg półtłusty", 1);
        addPublicIngredient(cottageCheeseSpread, "Szczypiorek", 0.1);
        addPublicIngredient(cottageCheeseSpread, "Śmietanka 34%", 0.25);
        addPublicIngredient(cottageCheeseSpread, "Sól", 0);
        addPublicIngredient(cottageCheeseSpread, "Pieprz", 0);
        cottageCheeseSpread.getRecipe().setDescription("Szczypiorek drobno posiekaj. Wymieszaj wszystkie składniki do uzyskania lekko luźnej konsystencji");
        saveAndStore(cottageCheeseSpread, intermediates);

        Item eggSpread = new Item("Pasta jajeczna", KILOGRAM, INTERMEDIATE, user);
        addPublicIngredient(eggSpread, "Jajko", 12);
        addPublicIngredient(eggSpread, "Majonez", 0.2);
        addPublicIngredient(eggSpread, "Sól", 0);
        addPublicIngredient(eggSpread, "Pieprz", 0);
        eggSpread.getRecipe().setDescription("Jajka ugotuj na twardo (~6 minut w lekko wrzącej wodzie), wystudź, obierz, zetrzyj na tarce. Dodaj majonez, wymieszaj, dopraw");
        eggSpread.getRecipe().setRecipeYield(BigDecimal.valueOf(0.8));
        saveAndStore(eggSpread, intermediates);

        Item tunaSpread = new Item("Pasta z tuńczyka", KILOGRAM, INTERMEDIATE, user);
        addPublicIngredient(tunaSpread, "Tuńczyk w zalewie", 1);
        addPublicIngredient(tunaSpread, "Majonez", 0.2);
        addPublicIngredient(tunaSpread, "Sól", 0);
        addPublicIngredient(tunaSpread, "Pieprz", 0);
        tunaSpread.getRecipe().setDescription("Tuńczyka porządnie odcisnąć z zalewy. Dodać majonez, wymieszać, doprawić");
        tunaSpread.getRecipe().setRecipeYield(BigDecimal.valueOf(0.8));
        saveAndStore(tunaSpread, intermediates);

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
        saveAndStore(chilliJam, intermediates);

        Item aioli = new Item("Aioli", KILOGRAM, INTERMEDIATE, user);
        addPublicIngredient(aioli, "Majonez", 1);
        addPublicIngredient(aioli, "Czosnek obrany", 0.5);
        addPublicIngredient(aioli, "Sól", 0.01);
        addPublicIngredient(aioli, "Pieprz", 0.01);
        addPublicIngredient(aioli, "Olej", 0.2);
        aioli.getRecipe().setDescription("Czosnek usmaż w dużej ilości oleju na niskim ogniu do miękkości. Po usmażeniu nadmiar oleju wylej. Zblenduj, wymieszaj z majonezem, dopraw");
        aioli.getRecipe().setRecipeYield(BigDecimal.valueOf(1.6));
        saveAndStore(aioli, intermediates);

        Item pomodoro = new Item("Sos pomodoro", KILOGRAM, INTERMEDIATE, user);
        addPublicIngredient(pomodoro, "Pomidory San Marzano", 2.5);
        addPublicIngredient(pomodoro, "Sól", 0.01);
        addPublicIngredient(pomodoro, "Oliwa z oliwek", 0.2);
        pomodoro.getRecipe().setRecipeYield(BigDecimal.valueOf(2.5));
        pomodoro.getRecipe().setDescription("Pomidory zblenduj delikatnie (powinny zostać małe kawałki). Dodaj oliwę i sól.");
        saveAndStore(pomodoro, intermediates);

        return intermediates;
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

        Item eggRoll = new Item("Bułka z pastą jajeczną", null, DISH, user);
        addPublicIngredient(eggRoll, "Bułka maślana", 1);
        addPublicIngredient(eggRoll, "Sałata rzymska", 0.01);
        eggRoll.addIngredient(intermediates.get("Pasta jajeczna"), BigDecimal.valueOf(0.05));
        eggRoll.getRecipe().setDescription("Bułkę rozkrój i podpiecz. Posmaruj pastą, dodaj posiekaną sałatę. Wydawaj na małym talerzu.");
        saveAndStore(eggRoll, dishIds);

        Item tunaRoll = new Item("Bułka z pastą z tuńczyka", null, DISH, user);
        addPublicIngredient(tunaRoll, "Jajko", 1);
        addPublicIngredient(tunaRoll, "Sałata rzymska", 0.05);
        addPublicIngredient(tunaRoll, "Bułka maślana", 1);
        tunaRoll.addIngredient(intermediates.get("Pasta z tuńczyka"), BigDecimal.valueOf(0.05));
        saveAndStore(tunaRoll, dishIds);

        Item hamAndCheeseRoll = new Item("Bułka z szynką i pieczarkami", null, DISH, user);
        addPublicIngredient(hamAndCheeseRoll, "Bułka maślana", 1);
        addPublicIngredient(hamAndCheeseRoll, "Pieczarka", 0.02);
        addPublicIngredient(hamAndCheeseRoll, "Szynka Cotto", 0.02);
        addPublicIngredient(hamAndCheeseRoll, "Cheddar", 0.01);
        addPublicIngredient(hamAndCheeseRoll, "Sałata rzymska", 0.01);
        addPublicIngredient(hamAndCheeseRoll, "Olej", 0);
        hamAndCheeseRoll.addIngredient(intermediates.get("Aioli"), BigDecimal.valueOf(0.01));
        hamAndCheeseRoll.addIngredient(intermediates.get("Sos pomodoro"), BigDecimal.valueOf(0.01));
        hamAndCheeseRoll.getRecipe().setDescription("Pieczarki i szynkę posiekaj w plastry 1mm x 2cm x 3cm, usmaż na niewielkiej ilości oleju. Bułkę rozkrój, jedną część posmaruj pomodoro, drugą aioli. Na jedną część połóż plaster cheddara. Obie części podpiecz. Sałatę i mix z szynki i pieczarek wyłóż na bułkę i zamknij.");
        saveAndStore(hamAndCheeseRoll, dishIds);

        Item omelette = new Item("Omlet górski", null, DISH, user);
        addPublicIngredient(omelette, "Jajko", 2);
        addPublicIngredient(omelette, "Szynka Cotto", 0.01);
        addPublicIngredient(omelette, "Taleggio", 0.01);
        addPublicIngredient(omelette, "Provolone", 0.01);
        addPublicIngredient(omelette, "Grana Padano", 0.005);
        addPublicIngredient(omelette, "Foccacia", 1);
        addPublicIngredient(omelette, "Szczypiorek", 0);
        addPublicIngredient(omelette, "Olej", 0);
        omelette.addIngredient(intermediates.get("Dżem z chilli"), BigDecimal.valueOf(0.01));
        omelette.getRecipe().setDescription("Szynkę podsmaż na oleju na małej patelni, dodaj rozbełtane jajka. Gdy jajka będą w 75% ścięte, posyp serami i dodaj kilka kleksów dżemu z chilli. Złóż w trójkąt i połóż na podpieczonej foccacii. Posyp posiekanym szczypiorkiem.");
        saveAndStore(omelette, dishIds);

        Item shakshuka = new Item("Shakshuka z kozim serem", null, DISH, user);
        addPublicIngredient(shakshuka, "Jajko", 2);
        addPublicIngredient(shakshuka, "Szczypiorek", 0);
        addPublicIngredient(shakshuka, "Foccacia", 1);
        addPublicIngredient(shakshuka, "Ser kozi pleśniowy", 0.01);
        addPublicIngredient(shakshuka, "Sól", 0);
        addPublicIngredient(shakshuka, "Pieprz", 0);
        shakshuka.addIngredient(intermediates.get("Shakshuka baza"), BigDecimal.valueOf(0.2));
        shakshuka.getRecipe().setDescription("Bazę podgrzej, jajka zrób sadzone. Bazę wlej do miski, na wierzch połóż jajka, posyp kozim serem i szczypiorkiem");
        saveAndStore(shakshuka, dishIds);


        return dishIds;
    }

    private void saveAndStore(Item item, List<Long> dishIds) {
        item = itemRepository.save(item);
        dishIds.add(item.getId());
    }

    private void saveAndStore(Item item, Map<String, Item> intermediateIds) {
        item = itemRepository.save(item);
        intermediateIds.put(item.getName(), item);
    }


    private void addPublicIngredient(Item item, String itemName, double amount) {
        Optional<Item> toAdd = itemRepository.findPublicItemByName(itemName);
        if (toAdd.isEmpty()) return;
        item.addIngredient(toAdd.get(), BigDecimal.valueOf(amount));
    }
}
