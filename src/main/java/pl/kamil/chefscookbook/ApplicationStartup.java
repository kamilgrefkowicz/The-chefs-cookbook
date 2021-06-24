package pl.kamil.chefscookbook;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.*;
import static pl.kamil.chefscookbook.user.domain.MasterUserConfig.getMasterUser;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private UserEntity master;

    @Override
    public void run(String... args) throws Exception {


        initializeUser();
        initializeStartingItems();

    }

    private void initializeUser() {
        master = getMasterUser();
        if (userRepository.findById(1L).isEmpty()) {
        userRepository.save(master);
        }
    }


    private void initializeStartingItems() {


        savePublicBasicItem("Ziemniak", KILOGRAM);
        savePublicBasicItem("Masło", KILOGRAM);
        savePublicBasicItem("Bułka maślana", PIECE);
        savePublicBasicItem("Twaróg półtłusty", KILOGRAM);
        savePublicBasicItem("Sałata rzymska", KILOGRAM);
        savePublicBasicItem("Rzodkiewka", PIECE);
        savePublicBasicItem("Ogórek świeży", KILOGRAM);
        savePublicBasicItem("Jajko", PIECE);
        savePublicBasicItem("Woda", LITRE);
        savePublicBasicItem("Śmietanka 34%", LITRE);
        savePublicBasicItem("Jogurt naturalny", KILOGRAM);
        savePublicBasicItem("Majonez", KILOGRAM);
        savePublicBasicItem("Szynka Cotto", KILOGRAM);
        savePublicBasicItem("Cheddar", KILOGRAM);
        savePublicBasicItem("Pieczarka", KILOGRAM);
        savePublicBasicItem("Pomidory pelati", KILOGRAM);
        savePublicBasicItem("Pomidory San Marzano", KILOGRAM);
        savePublicBasicItem("Boczek parzony", KILOGRAM);
        savePublicBasicItem("Tuńczyk w zalewie", KILOGRAM);
        savePublicBasicItem("Pomidor", KILOGRAM);
        savePublicBasicItem("Chilli", KILOGRAM);
        savePublicBasicItem("Czosnek obrany", KILOGRAM);
        savePublicBasicItem("Pieczarka Portobello", KILOGRAM);
        savePublicBasicItem("Papryka kolorowa", KILOGRAM);
        savePublicBasicItem("Ciecierzyca", KILOGRAM);
        savePublicBasicItem("Seler naciowy", KILOGRAM);
        savePublicBasicItem("Salsiccia piccante", KILOGRAM);
        savePublicBasicItem("Ser kozi pleśniowy", KILOGRAM);
        savePublicBasicItem("Awokado", PIECE);
        savePublicBasicItem("Parówka", PIECE);
        savePublicBasicItem("Ketchup", KILOGRAM);
        savePublicBasicItem("Szpinak liście", KILOGRAM);
        savePublicBasicItem("Szpinak baby", KILOGRAM);
        savePublicBasicItem("Frankfurterka", PIECE);
        savePublicBasicItem("Fasolka w sosie pomidorowym", KILOGRAM);
        savePublicBasicItem("Grana Padano", KILOGRAM);
        savePublicBasicItem("Provolone", KILOGRAM);
        savePublicBasicItem("Taleggio", KILOGRAM);
        savePublicBasicItem("Rukola", KILOGRAM);
        savePublicBasicItem("Ocet winny", LITRE);
        savePublicBasicItem("Miód", LITRE);
        savePublicBasicItem("Musztarda słoneczna", PIECE);
        savePublicBasicItem("Wątróbka drobiowa", KILOGRAM);
        savePublicBasicItem("Chrzan", KILOGRAM);
        savePublicBasicItem("Biała kiełbaska", PIECE);
        savePublicBasicItem("Mąka do pizzy", KILOGRAM);
        savePublicBasicItem("Mozzarella Fior di Latte", KILOGRAM);
        savePublicBasicItem("Sól", KILOGRAM);
        savePublicBasicItem("Pieprz", KILOGRAM);
        savePublicBasicItem("Szczypiorek", KILOGRAM);
        savePublicBasicItem("Tymianek", KILOGRAM);
        savePublicBasicItem("Rozmaryn", KILOGRAM);
        savePublicBasicItem("Schab", KILOGRAM);
        savePublicBasicItem("Cebula czerwona", KILOGRAM);
        savePublicBasicItem("Cebula żółta", KILOGRAM);
        savePublicBasicItem("Kmin rzymski", KILOGRAM);
        savePublicBasicItem("Oliwa z oliwek", LITRE);
        savePublicBasicItem("Cukier", KILOGRAM);
        savePublicBasicItem("Imbir", KILOGRAM);
        savePublicBasicItem("Sos sojowy", LITRE);
        savePublicBasicItem("Papryka czerwona", KILOGRAM);
        savePublicBasicItem("Olej", LITRE);
        savePublicBasicItem("Foccacia", PIECE);


    }

    private void savePublicBasicItem(String name, Unit unit) {
        if (itemRepository.findPublicItemByName(name).isPresent()) return;
        Item item = new Item(name, unit, BASIC, master);
        itemRepository.save(item);
    }

}
