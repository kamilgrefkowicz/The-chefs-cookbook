package pl.kamil.chefscookbook;

import ch.qos.logback.core.subst.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;


import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;


import java.security.Principal;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.DISH;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.*;
import static pl.kamil.chefscookbook.user.domain.MasterUserConfig.getMasterUser;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final ModifyItemUseCase modifyItemService;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        initializeUser();
        initializeStartingItems();

    }

    private void initializeUser() {
        UserEntity ccb = getMasterUser();
        UserEntity user = new UserEntity("kamil", "$2y$12$E1stjX9Ae8Zi8RWHPtUkl.w5046b9GIdgml6maQvjLXdtE0fZb7Be");
        userRepository.save(ccb).getId();
        userRepository.save(user).getId();
    }



    private void initializeStartingItems() {

        Type.valueOf("DISH");

        Principal ccb = () -> "1";
        modifyItemService.createItem(new CreateNewItemCommand("Ziemniak", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Masło", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Bułka maślana", BASIC, PIECE), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Twaróg półtłusty", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Sałata rzymska", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Rzodkiewka", BASIC, PIECE), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ogórek świeży", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Jajko", BASIC, PIECE), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Woda", BASIC, LITRE), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Śmietanka 34%", BASIC, LITRE), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Jogurt naturalny", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Majonez", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Szynka Cotto", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Cheddar", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pieczarka", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pomidory pelati", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pomidory San Marzano", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Boczek parzony", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Tuńczyk w zalewie", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pomidor", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Chilli", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Czosnek obrany", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pieczarka Portobello", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Papryka kolorowa", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ciecierzyca", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Seler naciowy", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Salsiccia piccante", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ser kozi pleśniowy", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Awokado", BASIC, PIECE), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Parówka", BASIC, PIECE), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ketchup", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Szpinak liście", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Szpinak baby", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Frankfurterka", BASIC, PIECE), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Fasolka w sosie pomidorowym", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Grana Padano", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Provolone", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Taleggio", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Rukola", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Ocet winny", BASIC, LITRE), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Miód", BASIC, LITRE), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Musztarda słoneczna", BASIC, PIECE), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Wątróbka drobiowa", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Chrzan", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Biała kiełbaska", BASIC, PIECE), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Mąka do pizzy", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Mozzarella Fior di Latte", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Sól", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Pieprz", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Szczypiorek", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Tymianek", BASIC, KILOGRAM), ccb);
        modifyItemService.createItem(new CreateNewItemCommand("Rozmaryn", BASIC, KILOGRAM), ccb);

    }
}
