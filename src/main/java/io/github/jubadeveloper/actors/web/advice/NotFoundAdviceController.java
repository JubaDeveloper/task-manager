package io.github.jubadeveloper.actors.web.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class NotFoundAdviceController {
    @ExceptionHandler(
            NoHandlerFoundException.class
    )
    public String redirectToLoginOnNotFound () {
        return "login";
    }
}
