package pl.kamil.chefscookbook.food.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.kamil.chefscookbook.food.database.IngredientRepository;
import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import javax.transaction.Transactional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ModifyItemServiceTest {

    @Autowired
    ModifyItem modifyItem;

    @Autowired
    QueryItem queryItem;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    UserEntity user;




}