package io.github.jubadeveloper.actors.web.advice;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ForbiddenAdviceController {
    @ExceptionHandler(AccessDeniedException.class)
    public String forbiddenExceptionHandler () {
        return "errors/forbidden";
    }
}
