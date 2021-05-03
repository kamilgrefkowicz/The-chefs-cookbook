package pl.kamil.chefscookbook.food.application.port;

import lombok.Value;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface ModifyItemUseCase {

    void createItem(CreateNewItemCommand command);



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
}
