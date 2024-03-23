package io.github.jubadeveloper.actors.web;

import io.github.jubadeveloper.core.meta.annotations.WebLayer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.thymeleaf.expression.Calendars;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@WebLayer("/login")
public class Login {
    @GetMapping
    public String getLogin () {
        return "login";
    }

    @ModelAttribute("today")
    public Calendar date () {
        return new Calendars(Locale.US).createToday();
    }
}
