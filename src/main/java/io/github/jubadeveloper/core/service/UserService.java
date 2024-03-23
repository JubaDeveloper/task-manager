package io.github.jubadeveloper.core.service;

import io.github.jubadeveloper.core.domain.User;
import io.github.jubadeveloper.core.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User loadById (Long id) {
        return userRepository
                .findById(id).
                orElse(null);
    }

    public User loadByEmail (String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser (User user) {
        return userRepository.save(user);
    }
}
