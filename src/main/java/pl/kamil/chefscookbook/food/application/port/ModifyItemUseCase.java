package pl.kamil.chefscookbook.food.application.port;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.shared.exception.LoopAttemptedException;
import pl.kamil.chefscookbook.shared.exception.NameAlreadyTakenException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public interface ModifyItemUseCase {

    ItemDto createItem(CreateNewItemCommand command) throws NameAlreadyTakenException;

    RichItem addIngredientToRecipe(AddIngredientCommand command, Long userId) throws LoopAttemptedException;

    RichItem setYield(SetYieldCommand command);

    RichItem updateDescription(UpdateDescriptionCommand command);

    void deleteItem(DeleteItemCommand command);

    RichItem removeIngredientFromRecipe(RemoveIngredientFromRecipeCommand command);


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class CreateNewItemCommand {
        @NotBlank
        private String itemName;
        @NotNull
        private int itemTypeId;
        @NotNull
        private int itemUnitId;

        private Long userId;


    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class AddIngredientCommand {
        @NotNull
        private Long parentItemId;
        @NotNull
        private Long childItemId;
        @Positive
        private BigDecimal amount;
    }

    @Data
    @AllArgsConstructor
    class SetYieldCommand {
        @NotNull
        private Long parentItemId;
        @Positive
        private BigDecimal itemYield;
    }

    @Data
    @AllArgsConstructor
    class UpdateDescriptionCommand {
        @NotNull
        private Long parentItemId;
        @NotBlank
        private String description;
    }

    @Data
    @AllArgsConstructor
    class DeleteItemCommand {
        @NotNull
        private Long itemId;
    }

    @Data
    @AllArgsConstructor
    class RemoveIngredientFromRecipeCommand {
        @NotNull
        private Long parentItemId;
        @NotNull
        private Long ingredientId;
    }
}
