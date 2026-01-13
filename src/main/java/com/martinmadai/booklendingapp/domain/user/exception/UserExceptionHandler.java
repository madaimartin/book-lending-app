package com.martinmadai.booklendingapp.domain.user.exception;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice(basePackages = "com.martinmadai.booklendingapp.domain.user")
@AllArgsConstructor
public class UserExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserExists(UserAlreadyExistsException ex,
                                   Model model,
                                   Locale locale) {

        String message = messageSource.getMessage(
                ex.getMessageKey(),
                null,
                locale
        );

        model.addAttribute("error", message);
        return "auth/register";
    }
}
