package com.martinmadai.booklendingapp.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class YearRangeValidator implements ConstraintValidator<YearRange, Integer> {

    private static final int MIN_YEAR = 1450;

    private int min;
    private int max;

    @Override
    public void initialize(YearRange annotation) {
        this.min = annotation.min();
        this.max = annotation.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        int currentYear = Year.now().getValue();
        int effectiveMax = Math.min(max, currentYear);

        return value >= min && value <= effectiveMax;
    }
}