package com.martinmadai.booklendingapp.common.exception;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Locale;

@ControllerAdvice
@AllArgsConstructor
public class ValidationExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidation(MethodArgumentNotValidException ex,
                                   Model model,
                                   Locale locale) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> messageSource.getMessage(error, locale))
                .toList();

        model.addAttribute("validationErrors", errors);
        return "auth/register";
    }
}
