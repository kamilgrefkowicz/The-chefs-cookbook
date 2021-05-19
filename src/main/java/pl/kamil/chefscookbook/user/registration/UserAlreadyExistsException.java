package pl.kamil.chefscookbook.user.registration;

public class UserAlreadyExistsException extends Throwable {

    public UserAlreadyExistsException(final String message) {
        super(message);
    }
}
