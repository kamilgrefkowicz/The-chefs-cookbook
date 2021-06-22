package pl.kamil.chefscookbook.food.application.port;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.shared.response.Response;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.security.Principal;

public interface ModifyItemService {

    Response<ItemDto> createItem(CreateNewItemCommand command, Principal user);

    Response<ItemDto> addIngredientToRecipe(AddIngredientCommand command, Principal user) ;

    Response<ItemDto> setYield(SetYieldCommand command, Principal user);

    Response<ItemDto> updateDescription(UpdateDescriptionCommand command, Principal user);

    Response<Void> deleteItem(DeleteItemCommand command, Principal user);

    Response<ItemDto> removeIngredientFromRecipe(RemoveIngredientFromRecipeCommand command, Principal user);


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class CreateNewItemCommand {

        @Length(min = 3, max = 15)
        private String itemName;

        @NotNull
        private Type type;

        private Unit unit;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class AddIngredientCommand {

        private Long parentItemId;
        private Long childItemId;

        @PositiveOrZero
        private BigDecimal amount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class SetYieldCommand {
        private Long parentItemId;
        @Positive
        private BigDecimal itemYield;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class UpdateDescriptionCommand {
        private Long parentItemId;
        @Length(max = 1000)
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class DeleteItemCommand {
        private Long itemId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class RemoveIngredientFromRecipeCommand {
        private Long parentItemId;
        private Long ingredientId;
    }
}
