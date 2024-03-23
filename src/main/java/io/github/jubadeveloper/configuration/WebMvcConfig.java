package io.github.jubadeveloper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.spring6.*;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
//@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    public ITemplateResolver springResourceTemplateResolver () {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver
                .addTemplateAlias("register", "register/register");
        templateResolver
                .addTemplateAlias("login", "login/login");
        templateResolver
                .addTemplateAlias("errors", "errors/*.html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);
        return templateResolver;
    }
    @Bean
    public SpringTemplateEngine springTemplateEngine (
            ResourceBundleMessageSource messageSource
    ) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setEnableSpringELCompiler(true);
        templateEngine.setTemplateResolver(springResourceTemplateResolver());
        templateEngine.setMessageSource(messageSource);
        return templateEngine;
    }
    @Bean
    public ViewResolver viewResolver (
            ResourceBundleMessageSource messageSource
    ) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(springTemplateEngine(messageSource));
        return viewResolver;
    }
}
