package com.example.laborator6.domain.validators;

import com.example.laborator6.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {

        String error = "";

        if (entity.getFirstName().length() < 3)
            error += "First name must have at least 3 characters\n";
        if (entity.getLastName().length() < 3)
            error += "Last name must have at least 3 characters\n";

        if (error.length() > 0)
            throw new ValidationException(error);

    }
}
