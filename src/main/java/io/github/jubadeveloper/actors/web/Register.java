package io.github.jubadeveloper.actors.web;

import io.github.jubadeveloper.core.domain.User;
import io.github.jubadeveloper.core.meta.annotations.WebLayer;
import io.github.jubadeveloper.core.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@WebLayer("/register")
public class Register {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LogManager.getLogger(Register.class);
    public Register (
            UserService userService,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public String registerGet (User user) {
        return "register";
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public String registerPost (User user, ModelMap modelMap, BindingResult bindingResult) {
        User existentUser = userService.loadByEmail(user.getEmail());
        if (bindingResult.hasErrors()) {
            modelMap.addAttribute("errors", bindingResult.getAllErrors());
            return "register";
        }
        if (existentUser != null) {
            modelMap.addAttribute("errors", List.of("User already exists with give email"));
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userService.createUser(user);
        logger.info("Created user: {}", registeredUser);
        return "login";
    }
}
