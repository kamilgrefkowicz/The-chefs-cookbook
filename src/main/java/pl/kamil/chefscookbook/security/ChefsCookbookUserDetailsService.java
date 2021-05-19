package pl.kamil.chefscookbook.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.kamil.chefscookbook.user.database.UserRepository;

@AllArgsConstructor
public class ChefsCookbookUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private final AdminConfig config;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (config.getUsername().equals(username)) return config.CCB();

        return userRepository.findByUsernameIgnoreCase(username)
                .map(UserEntityDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
