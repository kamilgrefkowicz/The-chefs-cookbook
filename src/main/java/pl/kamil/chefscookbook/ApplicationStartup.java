package pl.kamil.chefscookbook;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.AddIngredientCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.SetYieldCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;


import pl.kamil.chefscookbook.food.domain.staticData.TypeRepository;
import pl.kamil.chefscookbook.food.domain.staticData.UnitRepository;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;


import java.math.BigDecimal;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.*;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final QueryItemUseCase queryItemService;
    private final ModifyItemUseCase modifyItemService;
    private final UnitRepository unitRepository;
    private final TypeRepository typeRepository;
    private final UserRepository userRepository;
    private final UserEntity masterUser;

    private Long ccbId;
    private Long kamilId;

    @Override
    public void run(String... args) throws Exception {

        initializeTypesAndUnits();

        initializeUser();

        initializeStartingItems();
//


    }

    private void initializeUser() {
        UserEntity user = new UserEntity("kamil", "$2y$12$E1stjX9Ae8Zi8RWHPtUkl.w5046b9GIdgml6maQvjLXdtE0fZb7Be");
        UserEntity ccb = masterUser;
        kamilId = userRepository.save(user).getId();
        ccbId = userRepository.save(ccb).getId();
    }

    private void initializeTypesAndUnits() {
        unitRepository.save(KILOGRAM());
        unitRepository.save(LITRE());
        unitRepository.save(PIECE());

        typeRepository.save(BASIC());
        typeRepository.save(INTERMEDIATE());
        typeRepository.save(DISH());

    }

    @SneakyThrows
    private void initializeStartingItems() {


        ItemDto ziemniak = modifyItemService.createItem(new CreateNewItemCommand("ziemniak", 1, 1, ccbId ));
        ItemDto masło = modifyItemService.createItem(new CreateNewItemCommand("masło", 1, 1, ccbId));
        ItemDto puree = modifyItemService.createItem(new CreateNewItemCommand("puree", 2, 1, kamilId));
        ItemDto schab = modifyItemService.createItem(new CreateNewItemCommand("schab", 1, 1, ccbId));
        ItemDto schabZMaslemIPuree = modifyItemService.createItem(new CreateNewItemCommand("schabZMaslemIPuree", 3, 3, kamilId));

        AddIngredientCommand addZiemniaktoPuree = new AddIngredientCommand(puree.getId(), ziemniak.getId(), BigDecimal.valueOf(1));
        AddIngredientCommand addMasłoToPuree = new AddIngredientCommand(puree.getId(), masło.getId(), BigDecimal.valueOf(0.2));
//
        AddIngredientCommand addMasłoToSchab = new AddIngredientCommand(schabZMaslemIPuree.getId(), masło.getId(), BigDecimal.valueOf(0.1));
        AddIngredientCommand addPureeToSchab = new AddIngredientCommand(schabZMaslemIPuree.getId(), puree.getId(), BigDecimal.valueOf(0.4));
        AddIngredientCommand addSchabtoSchab = new AddIngredientCommand(schabZMaslemIPuree.getId(), schab.getId(), BigDecimal.valueOf(0.3));

        modifyItemService.setYield(new SetYieldCommand(puree.getId(), BigDecimal.valueOf(1.1)));
        modifyItemService.setYield(new SetYieldCommand(schabZMaslemIPuree.getId(), BigDecimal.ONE));


        modifyItemService.addIngredientToRecipe(addZiemniaktoPuree, kamilId);
        modifyItemService.addIngredientToRecipe(addMasłoToPuree, kamilId);
        modifyItemService.addIngredientToRecipe(addPureeToSchab, kamilId);
        modifyItemService.addIngredientToRecipe(addMasłoToSchab, kamilId);
        modifyItemService.addIngredientToRecipe(addSchabtoSchab, kamilId);


//        modifyItemService.deleteItem(new ModifyItemUseCase.DeleteItemCommand(ziemniak.getId()));
//        modifyItemService.removeIngredientFromRecipe(new RemoveIngredientFromRecipeCommand(puree.getId(), 3L));
    }
}
