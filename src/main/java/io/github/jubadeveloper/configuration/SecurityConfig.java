package io.github.jubadeveloper.configuration;


import io.github.jubadeveloper.core.filter.UserSessionFilter;
import io.github.jubadeveloper.core.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;
import java.util.logging.Filter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain globalSecurityFilterChain (
            HttpSecurity httpSecurity,
            UserService userService,
            UserSessionFilter userSessionFilter
    ) throws Exception {
        httpSecurity
                .addFilterBefore(userSessionFilter, AuthorizationFilter.class)
                .anonymous(httpSecurityAnonymousConfigurer -> httpSecurityAnonymousConfigurer
                        .principal("anonymous")
                        .authorities("ROLE_ANONYMOUS")
                )
                .authenticationManager(providerManager(userService))
                .userDetailsService(userDetailsService(userService))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers(new AntPathRequestMatcher("/panel/**")).authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginPage("/login")
                        .defaultSuccessUrl("/panel/task")
                        .permitAll()
                )
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )
        ;
        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
         return new io.github.jubadeveloper.core.service.UserDetailsService(userService);
    }


    @Bean
    public AuthenticationProvider daoAuthenticationProvider (UserService userService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService(userService));
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager providerManager (UserService userService) {
        return new ProviderManager(List.of(daoAuthenticationProvider(userService)));
    }


    @Bean     // Roles hierarchy
    public RoleHierarchy roleHierarchy () {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("""
                               ROLE_ADMIN > ROLE_USER
                               ROLE_USER > ROLE_ANONYMOUS
                               """);
        return hierarchy;
    }

    @Bean // Set the role hierarchy in method authorization as well
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
       return new BCryptPasswordEncoder();
    }
}
