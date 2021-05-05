package pl.kamil.chefscookbook.food.application.port;

import lombok.Value;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.PoorItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.RichItem;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.shared.response.Response;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public interface ModifyItemUseCase {

    RichItem createItem(CreateNewItemCommand command);

    RichItem addIngredientToRecipe(AddIngredientCommand command);


    @Value
    class CreateNewItemCommand {
        @NotBlank
        String itemName;
        @NotNull
        Type itemType;

        public Item toItem() {
            return  Item.builder()
                    .name(itemName)
                    .type(itemType)
                    .build();
        }
    }

    @Value
    class AddIngredientCommand {
        @NotNull
        Long parentItemId;
        @NotNull
        Long childItemId;
        @Positive
        BigDecimal amount;
    }
}
