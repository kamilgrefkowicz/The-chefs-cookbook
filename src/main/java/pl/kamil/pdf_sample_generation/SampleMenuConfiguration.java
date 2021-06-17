package pl.kamil.pdf_sample_generation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.PIECE;

@Configuration
public class SampleMenuConfiguration {

    UserEntity user = new UserEntity();
    Map<Integer, Item> tempBasics = new LinkedHashMap<>();
    Map<Integer, Item> tempIntermediates = new LinkedHashMap<>();

    Set<RichItem> dishes = new LinkedHashSet<>();


    @Bean
    public FullMenu fullMenu() {

        for (int i = 0; i < 30; i++) {
            tempBasics.put(i, new Item("basic" + i, KILOGRAM, BASIC, user));
        }

        for (int i = 0; i < 15; i++) {
            Item item = new Item("intermediate" + i, KILOGRAM, INTERMEDIATE, user);
            item.addIngredient(tempBasics.get(i), getMixedAmount(i));
            item.addIngredient(tempBasics.get(i % 7), getMixedAmount(i));
            item.getRecipe().setDescription(mixedLenghtDescription(i));
            tempIntermediates.put(i, item);
        }
        for (int i = 0; i < 10; i++) {
            Item item = new Item("dish " + i, PIECE, DISH, user);
            item.addIngredient(tempBasics.get(i % 7), getMixedAmount(i));
            item.addIngredient(tempIntermediates.get((i + 1) % 7), getMixedAmount(i));
            item.getRecipe().setDescription(mixedLenghtDescription(i));
            dishes.add(new RichItem(item));
        }
        Set<PoorItem> basics = new LinkedHashSet<>();

        tempBasics.values().stream()
                .forEach(item -> basics.add(new PoorItem(item)));

        Set<RichItem> intermediates = new LinkedHashSet<>();
        tempIntermediates.values().stream()
                .forEach(item -> intermediates.add(new RichItem(item)));

        return new FullMenu(new Menu("Test menu", user), dishes, intermediates, basics);
    }

    private BigDecimal getMixedAmount(int i) {
        return new BigDecimal("0.2").multiply(BigDecimal.valueOf(i));
    }

    private String mixedLenghtDescription(int i) {
        StringBuilder builder = new StringBuilder();
        String template = "this is a test; ";
        for (int j = 0; j < (i + 1) % 7 + 1; j++) {
            builder.append(template);
        }
        return builder.toString();
    }
}
