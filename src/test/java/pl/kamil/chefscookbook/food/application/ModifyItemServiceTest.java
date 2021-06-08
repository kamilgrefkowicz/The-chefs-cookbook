package pl.kamil.chefscookbook.food.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.kamil.chefscookbook.food.database.IngredientJpaRepository;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import javax.transaction.Transactional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ModifyItemServiceTest {

    @Autowired
    ModifyItemService modifyItem;

    @Autowired
    QueryItemService queryItem;

    @Autowired
    IngredientJpaRepository ingredientJpaRepository;

    @Autowired
    ItemJpaRepository itemJpaRepository;

    @Autowired
    UserRepository userRepository;

    UserEntity user;




}