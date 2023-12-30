package com.example.taskmanager.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Custom validator for the {@link ValidateTaskState} annotation.
 * This validator ensures that a task state is one of the specified values.
 */
public class TaskStateValidator implements ConstraintValidator<ValidateTaskState, CharSequence> {

    private List<String> acceptedValues;

    /**
     * Initializes the validator with accepted task state values.
     *
     * @param annotation The annotation instance.
     */
    @Override
    public void initialize(ValidateTaskState annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    /**
     * Validates whether the given task state is one of the accepted values.
     *
     * @param value    The task state value to validate.
     * @param context  The validation context.
     * @return true if the value is valid, false otherwise.
     */
    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value.toString());
    }
}