package pl.kamil.chefscookbook;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.RichItem;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.TypeRepository;
import pl.kamil.chefscookbook.food.domain.staticData.UnitRepository;

import java.math.BigDecimal;
import java.util.List;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.*;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final QueryItemUseCase queryItemService;
    private final ModifyItemUseCase modifyItemService;
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




        RichItem ziemniak = modifyItemService.createItem(new CreateNewItemCommand("ziemniak", BASIC()));
        RichItem masło = modifyItemService.createItem(new CreateNewItemCommand("masło", BASIC()));
        RichItem puree = modifyItemService.createItem(new CreateNewItemCommand("puree", INTERMEDIATE()));

        ModifyItemUseCase.AddIngredientCommand addZiemniak= new ModifyItemUseCase.AddIngredientCommand(3L, 2L, BigDecimal.valueOf(1));
        ModifyItemUseCase.AddIngredientCommand addMasło = new ModifyItemUseCase.AddIngredientCommand(3L, 2L, BigDecimal.valueOf(1));

        modifyItemService.addIngredientToRecipe(addZiemniak);
        modifyItemService.addIngredientToRecipe(addMasło);


        System.out.println(queryItemService.findAll().toString());



    }
}
