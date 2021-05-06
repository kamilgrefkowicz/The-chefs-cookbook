package pl.kamil.chefscookbook;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.AddIngredientCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.SetYieldCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.GetFullItemCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.PoorItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.RichItem;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.TypeRepository;
import pl.kamil.chefscookbook.food.domain.staticData.UnitRepository;


import java.math.BigDecimal;
import java.util.Map;

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

        initializeTypesAndUnits();

//        initialize();


    }

    private void initializeTypesAndUnits() {
        unitRepository.save(KILOGRAM());
        unitRepository.save(LITRE());
        unitRepository.save(PIECE());

        typeRepository.save(BASIC());
        typeRepository.save(INTERMEDIATE());
        typeRepository.save(DISH());

    }

    private void initialize() {


        RichItem ziemniak = modifyItemService.createItem(new CreateNewItemCommand("ziemniak", BASIC(), KILOGRAM()));
        RichItem masło = modifyItemService.createItem(new CreateNewItemCommand("masło", BASIC(), KILOGRAM()));
        RichItem puree = modifyItemService.createItem(new CreateNewItemCommand("puree", INTERMEDIATE(), KILOGRAM()));
        RichItem schab = modifyItemService.createItem(new CreateNewItemCommand("schab", BASIC(), KILOGRAM()));
        RichItem schabZMaslemIPuree =  modifyItemService.createItem(new CreateNewItemCommand("schabZMaslemIPuree", DISH(), PIECE()));

        AddIngredientCommand addZiemniaktoPuree = new AddIngredientCommand(puree.getId(), ziemniak.getId(), BigDecimal.valueOf(1));
        AddIngredientCommand addMasłoToPuree = new AddIngredientCommand(puree.getId(), masło.getId(), BigDecimal.valueOf(0.2));

        AddIngredientCommand addMasłoToSchab = new AddIngredientCommand(schabZMaslemIPuree.getId(), masło.getId(), BigDecimal.valueOf(0.1));
        AddIngredientCommand addPureeToSchab = new AddIngredientCommand(schabZMaslemIPuree.getId(), puree.getId(), BigDecimal.valueOf(0.4));
        AddIngredientCommand addSchabtoSchab = new AddIngredientCommand(schabZMaslemIPuree.getId(), schab.getId(), BigDecimal.valueOf(0.3));

        modifyItemService.setYield(new SetYieldCommand(puree.getId(), BigDecimal.valueOf(1.1)));
        modifyItemService.setYield(new SetYieldCommand(schabZMaslemIPuree.getId(), BigDecimal.ONE));


        modifyItemService.addIngredientToRecipe(addZiemniaktoPuree );
        modifyItemService.addIngredientToRecipe(addMasłoToPuree );
        modifyItemService.addIngredientToRecipe(addPureeToSchab );
        modifyItemService.addIngredientToRecipe(addMasłoToSchab);
        modifyItemService.addIngredientToRecipe(addSchabtoSchab);


        Map<PoorItem, BigDecimal> dependencyMapWithAmounts = queryItemService.getFullItem(new GetFullItemCommand(schabZMaslemIPuree.getId())).getDependencyMapWithAmounts();
        System.out.println(dependencyMapWithAmounts);

    }
}
