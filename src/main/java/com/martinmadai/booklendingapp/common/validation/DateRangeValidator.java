package com.martinmadai.booklendingapp.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<DateRange, LocalDate> {

    private int minYear;
    private boolean allowFuture;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.minYear = constraintAnnotation.minYear();
        this.allowFuture = constraintAnnotation.allowFuture();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        if (value.getYear() < minYear) {
            return false;
        }

        return allowFuture || !value.isAfter(LocalDate.now());
    }
}