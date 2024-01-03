package com.example.taskmanager.validation;

import com.example.taskmanager.data.TaskState;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom validation annotation to ensure that a task state is one of the specified values.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE, TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = TaskStateValidator.class)
@Documented
public @interface ValidateTaskState {
    Class<? extends Enum<?>> enumClass() default TaskState.class;
    String message() default "Must be TODO, IN_PROGRESS, COMPLETED or DELAYED";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
