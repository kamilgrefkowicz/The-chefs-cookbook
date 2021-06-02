package pl.kamil.chefscookbook;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.AddIngredientCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;


import pl.kamil.chefscookbook.food.domain.staticData.TypeRepository;
import pl.kamil.chefscookbook.food.domain.staticData.UnitRepository;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;


import java.math.BigDecimal;
import java.security.Principal;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.*;
import static pl.kamil.chefscookbook.user.domain.MasterUserConfig.getMasterUser;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final ModifyItemUseCase modifyItemService;
    private final UnitRepository unitRepository;
    private final TypeRepository typeRepository;
    private final UserRepository userRepository;

    private Long ccbId;
    private Long kamilId;

    @Override
    public void run(String... args) throws Exception {

//        initializeTypesAndUnits();
//
//        initializeUser();
//
//        initializeStartingItems();



    }

    private void initializeUser() {
        UserEntity ccb = getMasterUser();
        UserEntity user = new UserEntity("kamil", "$2y$12$E1stjX9Ae8Zi8RWHPtUkl.w5046b9GIdgml6maQvjLXdtE0fZb7Be");
        ccbId = userRepository.save(ccb).getId();
        kamilId = userRepository.save(user).getId();
    }

    private void initializeTypesAndUnits() {
        unitRepository.save(KILOGRAM());
        unitRepository.save(LITRE());
        unitRepository.save(PIECE());

        typeRepository.save(BASIC());
        typeRepository.save(INTERMEDIATE());
        typeRepository.save(DISH());

    }

    private void initializeStartingItems() {


        Principal ccb = () -> "1";
        modifyItemService.createItem(new CreateNewItemCommand("Ziemniak", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Masło", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Bułka maślana", 1, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Twaróg półtłusty", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Sałata rzymska", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Rzodkiewka", 1, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ogórek świeży", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Jajko", 1, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Woda", 1, 2), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Śmietanka 34%", 1, 2), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Jogurt naturalny", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Majonez", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Szynka Cotto", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Cheddar", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pieczarka", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pomidory pelati", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pomidory San Marzano", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Boczek parzony", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Tuńczyk w zalewie", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pomidor", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Chilli", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Czosnek obrany", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pieczarka Portobello", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Papryka kolorowa", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ciecierzyca", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Seler naciowy", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Salsiccia piccante", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ser kozi pleśniowy", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Awokado", 1, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Parówka", 1, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ketchup", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Szpinak liście", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Szpinak baby", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Frankfurterka", 1, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Fasolka w sosie pomidorowym", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Grana Padano", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Provolone", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Taleggio", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Rukola", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ocet winny", 1, 2), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Miód", 1, 2), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Musztarda słoneczna", 1, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Wątróbka drobiowa", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Chrzan", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Biała kiełbaska", 1, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Mąka do pizzy", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Mozzarella Fior di Latte", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Sól", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pieprz", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Szczypiorek", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Tymianek", 1, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Rozmaryn", 1, 1), ccb);

    }
}
