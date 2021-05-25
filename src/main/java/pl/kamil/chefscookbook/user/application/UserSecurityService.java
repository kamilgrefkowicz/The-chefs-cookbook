package pl.kamil.chefscookbook.user.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;

import java.security.Principal;

@Service
@AllArgsConstructor
public class UserSecurityService {

    private final ItemJpaRepository itemRepository;


    public boolean isOwner(Long itemId, Principal user) {
        return Long.valueOf(user.getName()).equals(itemRepository.getOne(itemId).getUserEntity().getId());
    }
}
