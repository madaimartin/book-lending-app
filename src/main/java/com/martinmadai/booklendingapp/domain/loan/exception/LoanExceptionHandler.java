package com.martinmadai.booklendingapp.domain.loan.exception;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice(basePackages = "com.martinmadai.booklendingapp.domain.loan")
@RequiredArgsConstructor
public class LoanExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler({
            BookCopyNotAvailableException.class,
            LoanNotFoundException.class,
            UnauthorizedLoanAccessException.class
    })
    public String handleLoanExceptions(RuntimeException ex,
                                       Model model,
                                       Locale locale) {

        String message = messageSource.getMessage(
                ex.getMessage(),
                null,
                locale
        );

        model.addAttribute("error", message);
        return "loan/error";
    }

    @ExceptionHandler(OptimisticLockException.class)
    public String handleOptimisticLock(RuntimeException ex,
                                        Model model,
                                        Locale locale) {

        String message = messageSource.getMessage(
                ex.getMessage(),
                null,
                locale
        );

        model.addAttribute("error", message);
        return "loan/error";
    }
}
