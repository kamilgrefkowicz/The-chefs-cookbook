package pl.kamil.chefscookbook.food.application.port;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.shared.response.Response;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.security.Principal;

public interface ModifyItemUseCase {

    Response<ItemDto> createItem(CreateNewItemCommand command, Principal user);

    Response<RichItem> addIngredientToRecipe(AddIngredientCommand command, Principal user) ;

    Response<RichItem> setYield(SetYieldCommand command, Principal user);

    Response<RichItem> updateDescription(UpdateDescriptionCommand command, Principal user);

    Response<Void> deleteItem(DeleteItemCommand command, Principal user);

    Response<RichItem> removeIngredientFromRecipe(RemoveIngredientFromRecipeCommand command, Principal user);


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class CreateNewItemCommand {
        @NotBlank
        private String itemName;
        @NotNull
        private Type type;
        @NotNull
        private Unit unit;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class AddIngredientCommand {
        @NotNull
        private Long parentItemId;
        @NotNull
        private Long childItemId;
        @PositiveOrZero
        private BigDecimal amount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class SetYieldCommand {
        @NotNull
        private Long parentItemId;
        @Positive
        private BigDecimal itemYield;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class UpdateDescriptionCommand {
        @NotNull
        private Long parentItemId;
        @NotBlank
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class DeleteItemCommand {
        @NotNull
        private Long itemId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class RemoveIngredientFromRecipeCommand {
        @NotNull
        private Long parentItemId;
        @NotNull
        private Long ingredientId;
    }
}
