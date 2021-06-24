package pl.kamil.chefscookbook.user.application.port;

import pl.kamil.chefscookbook.user.domain.UserEntity;

public interface FillTestUserWithRecipesUseCase {

    void execute(UserEntity user);
}
