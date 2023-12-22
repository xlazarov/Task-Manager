package com.example.taskmanager.validation;

import com.example.taskmanager.data.AppUser;
import com.example.taskmanager.data.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistsInDbValidator implements ConstraintValidator<ExistsInDb, AppUser> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(AppUser user, ConstraintValidatorContext context) {
        if (user == null) {
            return true;
        }
        return userRepository.existsById(user.getId());
    }
}
