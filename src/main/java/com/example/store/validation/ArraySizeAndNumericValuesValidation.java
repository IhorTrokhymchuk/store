package com.example.store.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

//THIS ANNOTATION USE FOR FIELD WHAT USE IN SEARCH BY PARAMETRS
public class ArraySizeAndNumericValuesValidation
        implements ConstraintValidator<ArraySizeAndNumericValues, String[]> {

    @Override
    public boolean isValid(String[] arrayField,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (arrayField == null) {
            return true;
        }
        if (arrayField.length != 1) {
            return false;
        }
        try {
            Double.parseDouble(arrayField[0]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
