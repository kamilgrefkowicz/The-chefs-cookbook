package pl.kamil.chefscookbook.food.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.annotation.DirtiesContext;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.user.database.UserRepository;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QueryItemServiceTest {

    @Autowired
    QueryItemService queryItem;

    @Autowired
    ModifyItemService modifyItem;

    @Autowired
    ItemJpaRepository itemRepository;

    @Autowired
    UserRepository userRepository;

}