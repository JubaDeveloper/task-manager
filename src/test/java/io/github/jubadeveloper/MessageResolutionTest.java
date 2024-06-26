package io.github.jubadeveloper;

import io.github.jubadeveloper.configuration.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
@DirtiesContext
public class MessageResolutionTest {
    @Autowired
    public ResourceBundleMessageSource messageSource;

    @Test
    public void shouldInjectMessageSource () {
        Assertions.assertThat(messageSource).isNotNull();
    }

    @Test
    public void shouldResolveMessage () {
        System.out.println(messageSource);
        String helloMessage = messageSource.getMessage("hello", new String[]{}, Locale.US);
        Assertions.assertThat(helloMessage).isEqualTo("Hello world!");
    }
}
