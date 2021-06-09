package pl.kamil.chefscookbook.food.application;

import org.springframework.beans.factory.annotation.Autowired;

import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.user.database.UserRepository;


class QueryItemServiceTest {

    @Autowired
    QueryItem queryItem;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

}