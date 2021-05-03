package pl.kamil.chefscookbook;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.entity.Recipe;
import pl.kamil.chefscookbook.food.domain.staticData.TypeRepository;
import pl.kamil.chefscookbook.food.domain.staticData.UnitRepository;

import java.math.BigDecimal;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.*;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final QueryItemUseCase queryItemService;
    private final ModifyItemUseCase createItemService;
    private final UnitRepository unitRepository;
    private final TypeRepository typeRepository;

    @Override
    public void run(String... args) throws Exception {

        unitRepository.save(KILOGRAM());
        unitRepository.save(LITRE());
        unitRepository.save(PIECE());

        typeRepository.save(BASIC());
        typeRepository.save(INTERMEDIATE());
        typeRepository.save(DISH());

        Item ziemniak = Item.builder()
                .name("ziemniak")
                .unit(KILOGRAM())
                .type(BASIC())
                .pricePerUnit(new BigDecimal(20))
                .build();

        Item masło = Item.builder()
                .name("masło")
                .type(BASIC())
                .unit(KILOGRAM())
                .pricePerUnit(new BigDecimal(20))
                .build();

        Item puree = Item.builder()
                .name("puree")
                .type(INTERMEDIATE())
                .unit(KILOGRAM())
                .pricePerUnit(new BigDecimal(20))
                .build();

        puree.setRecipe(new Recipe("ugotuj i zmiel", null,  puree, new BigDecimal(1)));


        createItemService.createItem(new CreateNewItemCommand("ziemniak", BASIC()));
        createItemService.createItem(new CreateNewItemCommand("masło", BASIC()));
        createItemService.createItem(new CreateNewItemCommand("puree", INTERMEDIATE()));

        System.out.println(queryItemService.findAll().toString());

    }
}
