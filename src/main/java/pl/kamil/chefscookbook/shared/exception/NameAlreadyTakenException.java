package pl.kamil.chefscookbook.shared.exception;

public class NameAlreadyTakenException extends Throwable {

    public NameAlreadyTakenException(final String message) {
        super(message);
    }
}
