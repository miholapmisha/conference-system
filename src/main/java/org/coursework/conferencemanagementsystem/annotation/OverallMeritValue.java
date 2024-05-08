package org.coursework.conferencemanagementsystem.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.coursework.conferencemanagementsystem.model.validation.OverallMeritValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OverallMeritValidator.class)
public @interface OverallMeritValue {

    String message() default "Value does not correspond to overall merit value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
