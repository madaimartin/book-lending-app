package com.martinmadai.booklendingapp.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneFieldNotEmptyValidator.class)
@Documented
public @interface AtLeastOneFieldNotEmpty {

    String message() default "{search.atLeastOneRequired}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
