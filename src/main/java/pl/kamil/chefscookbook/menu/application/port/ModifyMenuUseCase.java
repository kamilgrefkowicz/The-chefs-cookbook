package pl.kamil.chefscookbook.menu.application.port;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import pl.kamil.chefscookbook.menu.application.dto.PoorMenu;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.shared.response.Response;

import javax.validation.constraints.NotEmpty;
import java.security.Principal;

public interface ModifyMenuUseCase {

    Response<PoorMenu> createNewMenu(CreateNewMenuCommand command, Principal user);

    Response<RichMenu> addItemsToMenu(AddItemsToMenuCommand command, Principal user);

    Response<RichMenu> removeItemFromMenu(RemoveItemFromMenuCommand command, Principal user);

    Response<Void> deleteMenu(DeleteMenuCommand command, Principal user);


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class CreateNewMenuCommand {

        @NotEmpty
        @Length(min=3, max=20)
        private String menuName;
    }

    @Data
    @NoArgsConstructor
    class AddItemsToMenuCommand {

        private Long menuId;
        private Long[] itemIds;
    }

    @Data
    class RemoveItemFromMenuCommand {
        private Long menuId;
        private Long itemId;
    }

    @Data
    class DeleteMenuCommand {
        private Long menuId;
    }
}
