package pl.kamil.chefscookbook;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.port.CreateItemUseCase;
import pl.kamil.chefscookbook.food.application.port.QueryItemsUseCase;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.food.domain.staticData.UnitRepository;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final QueryItemsUseCase queryItemService;
    private final CreateItemUseCase createItemService;
    private final UnitRepository unitRepository;


    @Override
    public void run(String... args) throws Exception {

        unitRepository.save(Unit.KILOGRAM());
        unitRepository.save(Unit.LITRE());
        unitRepository.save(Unit.PIECE());

        Item pasztet = Item.builder()
                .name("pasztet")
                .unit(Unit.KILOGRAM())
                .pricePerUnit(new BigDecimal(20))
                .build();

        Item jajko = Item.builder()
                .name("jajko")
                .unit(Unit.PIECE())
                .pricePerUnit(new BigDecimal(20))
                .build();

        Item ziemniak = Item.builder()
                .name("ziemniak")
                .unit(Unit.KILOGRAM())
                .pricePerUnit(new BigDecimal(20))
                .build();


        createItemService.createItem(pasztet);
        createItemService.createItem(jajko);
        createItemService.createItem(ziemniak);

        System.out.println(queryItemService.findAll().toString());

    }
}
