package pl.kamil.chefscookbook;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.port.CreateItemUseCase;
import pl.kamil.chefscookbook.food.application.port.QueryItemsUseCase;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;

import javax.persistence.Column;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final QueryItemsUseCase queryItemService;
    private final CreateItemUseCase createItemService;


    @Override
    public void run(String... args) throws Exception {
        Item item1 = Item.builder()
                .name("pasztet")
                .unit(Unit.KILOGRAM())
                .pricePerUnit(new BigDecimal(20))
                .build();
        createItemService.createItem(item1);

        System.out.println(queryItemService.findAll().toString());

    }
}
