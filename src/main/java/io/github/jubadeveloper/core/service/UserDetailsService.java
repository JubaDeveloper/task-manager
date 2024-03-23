package io.github.jubadeveloper.core.service;

import io.github.jubadeveloper.core.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserService userService;
    private static final String EXCEPTION_MESSAGE = "User with email %s was not found.";
    private static final Logger logger = LogManager.getLogger(UserDetailsService.class);
    public UserDetailsService (UserService userService) {
        this.userService = userService;
    }
    @Override
    public UserDetails loadUserByUsername(
            String username // The user email
    ) throws UsernameNotFoundException {
        User user = userService.loadByEmail(username);
        if (user == null) {
            logger.info("User not found - Email: {}", username);
            throw new UsernameNotFoundException(String.format(EXCEPTION_MESSAGE, username));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles()
                    .stream().map(SimpleGrantedAuthority::new)
                    .toList()
        );
    }
}
