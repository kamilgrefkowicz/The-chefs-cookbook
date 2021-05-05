package pl.kamil.chefscookbook.food.application.port;

import lombok.Value;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.RichItem;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public interface ModifyItemUseCase {

    RichItem createItem(CreateNewItemCommand command);

    RichItem addIngredientToRecipe(AddIngredientCommand command);

    RichItem setYield(SetYieldCommand command);

    RichItem updateDescription(UpdateDescriptionCommand command);

    void deleteItem(DeleteItemCommand command);

    RichItem removeIngredientFromRecipe(RemoveIngredientFromRecipeCommand command);


    @Value
    class CreateNewItemCommand {
        @NotBlank
        String itemName;
        @NotNull
        Type itemType;
        @NotNull
        Unit itemUnit;

        public Item toItem() {
            return  Item.builder()
                    .name(itemName)
                    .type(itemType)
                    .unit(itemUnit)
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

    @Value
    class SetYieldCommand {
        @NotNull
        Long parentItemId;
        @Positive
        BigDecimal itemYield;
    }

    @Value
    class UpdateDescriptionCommand {
        @NotNull
        Long parentItemId;
        @NotBlank
        String description;
    }

    @Value
    class DeleteItemCommand {
        @NotNull
        Long itemId;
    }

    @Value
    class RemoveIngredientFromRecipeCommand {
        @NotNull
        Long parentItemId;
        @NotNull
        Long ingredientId;
    }
}
