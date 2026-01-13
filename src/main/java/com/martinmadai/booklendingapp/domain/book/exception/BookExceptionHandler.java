package com.martinmadai.booklendingapp.domain.book.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice(basePackages = "com.martinmadai.booklendingapp.domain.book")
@RequiredArgsConstructor
public class BookExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(BookNotFoundException.class)
    public String handleBookNotFound(BookNotFoundException ex,
                                     Model model,
                                     Locale locale) {

        String message = messageSource.getMessage(
                ex.getMessage(),
                null,
                locale
        );

        model.addAttribute("error", message);
        return "book/error";
    }
}
