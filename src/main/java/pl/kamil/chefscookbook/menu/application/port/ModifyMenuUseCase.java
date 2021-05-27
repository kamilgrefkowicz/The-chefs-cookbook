package pl.kamil.chefscookbook.menu.application.port;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import pl.kamil.chefscookbook.menu.application.dto.MenuDto;
import pl.kamil.chefscookbook.menu.application.dto.PoorMenu;
import pl.kamil.chefscookbook.shared.response.Response;

import javax.validation.constraints.NotEmpty;
import java.security.Principal;

public interface ModifyMenuUseCase {

    Response<PoorMenu> createNewMenu(CreateNewMenuCommand command, Principal user);

    Response<PoorMenu> addItemsToMenu(AddItemsToMenuCommand command, Principal user);

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
}
