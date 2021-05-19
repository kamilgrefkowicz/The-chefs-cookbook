package pl.kamil.chefscookbook.user.registration.password_validation;

import pl.kamil.chefscookbook.user.application.port.CreateUserUseCase.CreateUserCommand;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, CreateUserCommand> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(CreateUserCommand command, ConstraintValidatorContext constraintValidatorContext) {
        return command.getPassword().equals(command.getPasswordMatch());
    }
}

