package pl.kamil.chefscookbook.menu.application;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil.chefscookbook.menu.database.MenuRepository;
import pl.kamil.chefscookbook.user.application.port.UserSecurityService;

@ExtendWith(MockitoExtension.class)
class QueryMenuTest {

    @Mock
    MenuRepository menuRepository;
    @Mock
    UserSecurityService userSecurity;

    @InjectMocks
    QueryMenu queryMenu;

}















