package pl.kamil.chefscookbook;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;


import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.UnitRepository;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;


import java.security.Principal;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.*;
import static pl.kamil.chefscookbook.user.domain.MasterUserConfig.getMasterUser;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final ModifyItemUseCase modifyItemService;
    private final UnitRepository unitRepository;
    private final UserRepository userRepository;

    private Long ccbId;
    private Long kamilId;

    @Override
    public void run(String... args) throws Exception {

        initializeTypesAndUnits();

        initializeUser();

        initializeStartingItems();



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

    }

    private void initializeStartingItems() {


        Principal ccb = () -> "1";
        modifyItemService.createItem(new CreateNewItemCommand("Ziemniak", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Masło", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Bułka maślana", BASIC, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Twaróg półtłusty", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Sałata rzymska", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Rzodkiewka", BASIC, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ogórek świeży", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Jajko", BASIC, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Woda", BASIC, 2), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Śmietanka 34%", BASIC, 2), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Jogurt naturalny", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Majonez", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Szynka Cotto", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Cheddar", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pieczarka", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pomidory pelati", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pomidory San Marzano", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Boczek parzony", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Tuńczyk w zalewie", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pomidor", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Chilli", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Czosnek obrany", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pieczarka Portobello", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Papryka kolorowa", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ciecierzyca", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Seler naciowy", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Salsiccia piccante", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ser kozi pleśniowy", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Awokado", BASIC, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Parówka", BASIC, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ketchup", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Szpinak liście", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Szpinak baby", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Frankfurterka", BASIC, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Fasolka w sosie pomidorowym", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Grana Padano", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Provolone", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Taleggio", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Rukola", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ocet winny", BASIC, 2), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Miód", BASIC, 2), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Musztarda słoneczna", BASIC, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Wątróbka drobiowa", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Chrzan", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Biała kiełbaska", BASIC, 3), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Mąka do pizzy", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Mozzarella Fior di Latte", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Sól", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pieprz", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Szczypiorek", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Tymianek", BASIC, 1), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Rozmaryn", BASIC, 1), ccb);

    }
}
