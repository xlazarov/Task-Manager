package com.example.taskmanager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom validation annotation to ensure that a user is present in the database.
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ExistsInDbValidator.class)
@Documented
public @interface ExistsInDb {
    String message() default "User not present in table app_user";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
