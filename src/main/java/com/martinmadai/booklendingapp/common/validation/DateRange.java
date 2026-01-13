package com.martinmadai.booklendingapp.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateRange {

    String message() default "{validation.date.range}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int minYear() default 1900;

    boolean allowFuture() default true;
}