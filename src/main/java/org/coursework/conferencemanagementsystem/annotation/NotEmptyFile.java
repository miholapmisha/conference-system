package org.coursework.conferencemanagementsystem.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.coursework.conferencemanagementsystem.model.validation.NotEmptyFileValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotEmptyFileValidator.class)
public @interface NotEmptyFile {
    String message() default "Paper is empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}