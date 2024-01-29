package com.example.store.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

//THIS ANNOTATION USE FOR FIELD WHAT USE IN SEARCH BY PARAMETRS
public class ArrayNumericValuesValidation
        implements ConstraintValidator<ArrayNumericValues, String[]> {

    @Override
    public boolean isValid(String[] arrayField,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (arrayField == null) {
            return true;
        }
        for (String value : arrayField) {
            try {
                Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}
