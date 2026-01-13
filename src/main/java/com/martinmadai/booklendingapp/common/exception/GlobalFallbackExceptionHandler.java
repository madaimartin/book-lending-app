package com.martinmadai.booklendingapp.common.exception;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@AllArgsConstructor
public class GlobalFallbackExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {

        model.addAttribute("error", "Unexpected error occurred");
        return "error/general";
    }
}
