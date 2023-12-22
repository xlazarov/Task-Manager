package com.example.taskmanager.validation;

import com.example.taskmanager.data.AppUser;
import com.example.taskmanager.data.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom validator for the {@link ExistsInDb} annotation.
 * This validator checks whether an {@link AppUser} instance exists in the database.
 */
public class ExistsInDbValidator implements ConstraintValidator<ExistsInDb, AppUser> {
    @Autowired
    private UserRepository userRepository;

    /**
     * Validates whether the given {@link AppUser} instance exists in the database.
     *
     * @param user    The user to validate.
     * @param context The validation context.
     * @return true if the user exists in the database, false otherwise.
     */
    @Override
    public boolean isValid(AppUser user, ConstraintValidatorContext context) {
        if (user == null) {
            return true;
        }
        return userRepository.existsById(user.getId());
    }
}
