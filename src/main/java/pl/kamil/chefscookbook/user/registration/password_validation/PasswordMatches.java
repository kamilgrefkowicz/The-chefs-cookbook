package pl.kamil.chefscookbook.user.registration.password_validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
    String message() default "Podane hasła są różne";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
