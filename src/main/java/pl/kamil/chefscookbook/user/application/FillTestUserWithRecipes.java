package pl.kamil.chefscookbook.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.user.application.port.FillTestUserWithRecipesUseCase;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.math.BigDecimal;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.INTERMEDIATE;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;

@Component
@RequiredArgsConstructor
public class FillTestUserWithRecipes implements FillTestUserWithRecipesUseCase {

    private final ItemRepository itemRepository;

    @Override
    public void execute(UserEntity user) {

        generateIntermediates(user);
    }

    private void generateIntermediates(UserEntity user) {

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

        itemRepository.save(shakshukaBase);


    }



    private void addPublicIngredient(Item item, String s, double amount) {
        item.addIngredient(itemRepository.findPublicItemByName(s), BigDecimal.valueOf(amount));
    }
}
