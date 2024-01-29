package com.example.store.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//THIS ANNOTATION USE FOR FIELD WHAT USE IN SEARCH BY PARAMETRS
@Constraint(validatedBy = ArraySizeAndNumericValuesValidation.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ArraySizeAndNumericValues {
    String message() default "parameter must have size 1 and numeric value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
