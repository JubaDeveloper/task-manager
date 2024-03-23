package io.github.jubadeveloper;

import io.github.jubadeveloper.configuration.SecurityConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;



@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { Application.class, SecurityConfig.class })
public class ContextTest {
    @Autowired
    public ApplicationContext applicationContext;

    @Test
    public void testContextLoad () {
        Assertions.assertThat(applicationContext).isNotNull();
    }

}
