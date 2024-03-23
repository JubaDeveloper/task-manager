package io.github.jubadeveloper;

import io.github.jubadeveloper.configuration.AppConfig;
import io.github.jubadeveloper.core.domain.User;
import io.github.jubadeveloper.core.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppConfig.class})
public class RepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldLoadUserByEmail () {
        User user = userRepository.findByEmail("juba@gmail.com");
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getName()).isEqualTo("Juba");
        Assertions.assertThat(user.getRoles()).containsExactly("ROLE_ADMIN");
    }

    @Test
    public void shouldSaveAUser () {
        User user = userRepository.save(
                User.builder()
                        .email("juba@gm.com")
                        .password("juba2013")
                        .build()
                );
        Assertions.assertThat(userRepository.findByEmail("juba@gmail.com")).isNotNull();
        System.out.println(user);
    }
}
