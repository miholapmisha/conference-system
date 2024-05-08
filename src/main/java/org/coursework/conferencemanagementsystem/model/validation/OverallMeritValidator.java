package org.coursework.conferencemanagementsystem.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.coursework.conferencemanagementsystem.annotation.OverallMeritValue;
import org.coursework.conferencemanagementsystem.entity.entity_enum.OverallMerit;

public class OverallMeritValidator implements ConstraintValidator<OverallMeritValue, String> {

    @Override
    public void initialize(OverallMeritValue constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            OverallMerit.valueOf(value);
            return true;
        } catch (NullPointerException | IllegalArgumentException e) {
            return false;
        }
    }
}
