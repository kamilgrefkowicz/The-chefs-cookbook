package pl.kamil.chefscookbook;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.port.ModifyItemService;
import pl.kamil.chefscookbook.food.application.port.ModifyItemService.AddIngredientCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemService.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
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

    private final ModifyItemService modifyItemService;
    private final UserRepository userRepository;

    UserEntity user;

    @Override
    public void run(String... args) throws Exception {


        initializeUser();
        initializeStartingItems();

    }

    private void initializeUser() {
        UserEntity ccb = getMasterUser();
        user = new UserEntity("kamil", "$2y$12$E1stjX9Ae8Zi8RWHPtUkl.w5046b9GIdgml6maQvjLXdtE0fZb7Be");
        userRepository.save(ccb);
        userRepository.save(user);
    }


    private void initializeStartingItems() {

        Type.valueOf("DISH");

        Principal ccb = () -> "1";
        Principal kamil = () -> String.valueOf(user.getId());

        Response<ItemDto> ziemniak = modifyItemService.createItem(new CreateNewItemCommand("Ziemniak", BASIC, KILOGRAM), ccb);
        Response<ItemDto> masło = modifyItemService.createItem(new CreateNewItemCommand("Masło", BASIC, KILOGRAM), ccb);
        Response<ItemDto> bułka = modifyItemService.createItem(new CreateNewItemCommand("Bułka maślana", BASIC, PIECE), ccb);
        Response<ItemDto> twaróg = modifyItemService.createItem(new CreateNewItemCommand("Twaróg półtłusty", BASIC, KILOGRAM), ccb);
        Response<ItemDto> sałata = modifyItemService.createItem(new CreateNewItemCommand("Sałata rzymska", BASIC, KILOGRAM), ccb);
        Response<ItemDto> rzodkiewka = modifyItemService.createItem(new CreateNewItemCommand("Rzodkiewka", BASIC, PIECE), ccb);
        Response<ItemDto> ogórek = modifyItemService.createItem(new CreateNewItemCommand("Ogórek świeży", BASIC, KILOGRAM), ccb);
        Response<ItemDto> jajko = modifyItemService.createItem(new CreateNewItemCommand("Jajko", BASIC, PIECE), ccb);
        Response<ItemDto> woda = modifyItemService.createItem(new CreateNewItemCommand("Woda", BASIC, LITRE), ccb);
        Response<ItemDto> śmietanka = modifyItemService.createItem(new CreateNewItemCommand("Śmietanka 34%", BASIC, LITRE), ccb);
        Response<ItemDto> jogurt = modifyItemService.createItem(new CreateNewItemCommand("Jogurt naturalny", BASIC, KILOGRAM), ccb);
        Response<ItemDto> majonez = modifyItemService.createItem(new CreateNewItemCommand("Majonez", BASIC, KILOGRAM), ccb);
        Response<ItemDto> szynka = modifyItemService.createItem(new CreateNewItemCommand("Szynka Cotto", BASIC, KILOGRAM), ccb);
        Response<ItemDto> cheddar = modifyItemService.createItem(new CreateNewItemCommand("Cheddar", BASIC, KILOGRAM), ccb);
        Response<ItemDto> pieczarka = modifyItemService.createItem(new CreateNewItemCommand("Pieczarka", BASIC, KILOGRAM), ccb);
        Response<ItemDto> pomidoryPelati = modifyItemService.createItem(new CreateNewItemCommand("Pomidory pelati", BASIC, KILOGRAM), ccb);
        Response<ItemDto> pomidorySM = modifyItemService.createItem(new CreateNewItemCommand("Pomidory San Marzano", BASIC, KILOGRAM), ccb);
        Response<ItemDto> boczek = modifyItemService.createItem(new CreateNewItemCommand("Boczek parzony", BASIC, KILOGRAM), ccb);
        Response<ItemDto> tuńczyk = modifyItemService.createItem(new CreateNewItemCommand("Tuńczyk w zalewie", BASIC, KILOGRAM), ccb);
        Response<ItemDto> pomidor = modifyItemService.createItem(new CreateNewItemCommand("Pomidor", BASIC, KILOGRAM), ccb);
        Response<ItemDto> chilli = modifyItemService.createItem(new CreateNewItemCommand("Chilli", BASIC, KILOGRAM), ccb);
        Response<ItemDto> czosnek = modifyItemService.createItem(new CreateNewItemCommand("Czosnek obrany", BASIC, KILOGRAM), ccb);
        Response<ItemDto> portobello = modifyItemService.createItem(new CreateNewItemCommand("Pieczarka Portobello", BASIC, KILOGRAM), ccb);
        Response<ItemDto> papryka = modifyItemService.createItem(new CreateNewItemCommand("Papryka kolorowa", BASIC, KILOGRAM), ccb);
        Response<ItemDto> ciecierzyca = modifyItemService.createItem(new CreateNewItemCommand("Ciecierzyca", BASIC, KILOGRAM), ccb);
        Response<ItemDto> seler = modifyItemService.createItem(new CreateNewItemCommand("Seler naciowy", BASIC, KILOGRAM), ccb);
        Response<ItemDto> salsiccia = modifyItemService.createItem(new CreateNewItemCommand("Salsiccia piccante", BASIC, KILOGRAM), ccb);
        Response<ItemDto> kozi = modifyItemService.createItem(new CreateNewItemCommand("Ser kozi pleśniowy", BASIC, KILOGRAM), ccb);
        Response<ItemDto> awokado = modifyItemService.createItem(new CreateNewItemCommand("Awokado", BASIC, PIECE), ccb);
        Response<ItemDto> parówka = modifyItemService.createItem(new CreateNewItemCommand("Parówka", BASIC, PIECE), ccb);
        Response<ItemDto> ketchup = modifyItemService.createItem(new CreateNewItemCommand("Ketchup", BASIC, KILOGRAM), ccb);
        Response<ItemDto> szpinakLiście = modifyItemService.createItem(new CreateNewItemCommand("Szpinak liście", BASIC, KILOGRAM), ccb);
        Response<ItemDto> szpinakBaby = modifyItemService.createItem(new CreateNewItemCommand("Szpinak baby", BASIC, KILOGRAM), ccb);
        Response<ItemDto> frankfurterka = modifyItemService.createItem(new CreateNewItemCommand("Frankfurterka", BASIC, PIECE), ccb);
        Response<ItemDto> fasolka = modifyItemService.createItem(new CreateNewItemCommand("Fasolka w sosie pomidorowym", BASIC, KILOGRAM), ccb);
        Response<ItemDto> grana = modifyItemService.createItem(new CreateNewItemCommand("Grana Padano", BASIC, KILOGRAM), ccb);
        Response<ItemDto> provolone = modifyItemService.createItem(new CreateNewItemCommand("Provolone", BASIC, KILOGRAM), ccb);
        Response<ItemDto> taleggio = modifyItemService.createItem(new CreateNewItemCommand("Taleggio", BASIC, KILOGRAM), ccb);
        Response<ItemDto> rukola = modifyItemService.createItem(new CreateNewItemCommand("Rukola", BASIC, KILOGRAM), ccb);
        Response<ItemDto> ocet = modifyItemService.createItem(new CreateNewItemCommand("Ocet winny", BASIC, LITRE), ccb);
        Response<ItemDto> miód = modifyItemService.createItem(new CreateNewItemCommand("Miód", BASIC, LITRE), ccb);
        Response<ItemDto> musztarda = modifyItemService.createItem(new CreateNewItemCommand("Musztarda słoneczna", BASIC, PIECE), ccb);
        Response<ItemDto> wątróbka = modifyItemService.createItem(new CreateNewItemCommand("Wątróbka drobiowa", BASIC, KILOGRAM), ccb);
        Response<ItemDto> chrzan = modifyItemService.createItem(new CreateNewItemCommand("Chrzan", BASIC, KILOGRAM), ccb);
        Response<ItemDto> biała = modifyItemService.createItem(new CreateNewItemCommand("Biała kiełbaska", BASIC, PIECE), ccb);
        Response<ItemDto> mąka = modifyItemService.createItem(new CreateNewItemCommand("Mąka do pizzy", BASIC, KILOGRAM), ccb);
        Response<ItemDto> mozzarella = modifyItemService.createItem(new CreateNewItemCommand("Mozzarella Fior di Latte", BASIC, KILOGRAM), ccb);
        Response<ItemDto> sól = modifyItemService.createItem(new CreateNewItemCommand("Sól", BASIC, KILOGRAM), ccb);
        Response<ItemDto> pieprz = modifyItemService.createItem(new CreateNewItemCommand("Pieprz", BASIC, KILOGRAM), ccb);
        Response<ItemDto> szczypiorek = modifyItemService.createItem(new CreateNewItemCommand("Szczypiorek", BASIC, KILOGRAM), ccb);
        Response<ItemDto> tymianek = modifyItemService.createItem(new CreateNewItemCommand("Tymianek", BASIC, KILOGRAM), ccb);
        Response<ItemDto> rozmaryn = modifyItemService.createItem(new CreateNewItemCommand("Rozmaryn", BASIC, KILOGRAM), ccb);
        Response<ItemDto> schab = modifyItemService.createItem(new CreateNewItemCommand("Schab", BASIC, KILOGRAM), ccb);
        Response<ItemDto> cebula_czerwona = modifyItemService.createItem(new CreateNewItemCommand("Cebula czerwona", BASIC, KILOGRAM), ccb);
        Response<ItemDto> cebula_żółta = modifyItemService.createItem(new CreateNewItemCommand("Cebula żółta", BASIC, KILOGRAM), ccb);
        Response<ItemDto> kmin_rzymski = modifyItemService.createItem(new CreateNewItemCommand("Kmin rzymski", BASIC, KILOGRAM), ccb);
        Response<ItemDto> oliwa_z_oliwek = modifyItemService.createItem(new CreateNewItemCommand("Oliwa z oliwek", BASIC, LITRE), ccb);

        Response<ItemDto> puree = modifyItemService.createItem(new CreateNewItemCommand("Puree", INTERMEDIATE, KILOGRAM), kamil);
        Response<ItemDto> schab_z_puree = modifyItemService.createItem(new CreateNewItemCommand("Schab z puree", DISH, PIECE), kamil);
        modifyItemService.addIngredientToRecipe(new AddIngredientCommand(schab_z_puree.getData().getId(), puree.getData().getId(), new BigDecimal("0.2")), kamil);
        modifyItemService.addIngredientToRecipe(new AddIngredientCommand(schab_z_puree.getData().getId(), schab.getData().getId(), new BigDecimal("0.3")), kamil);
        modifyItemService.addIngredientToRecipe(new AddIngredientCommand(puree.getData().getId(), ziemniak.getData().getId(), BigDecimal.ONE), kamil);
        modifyItemService.addIngredientToRecipe(new AddIngredientCommand(puree.getData().getId(), masło.getData().getId(), new BigDecimal("0.2")), kamil);

    }
}
