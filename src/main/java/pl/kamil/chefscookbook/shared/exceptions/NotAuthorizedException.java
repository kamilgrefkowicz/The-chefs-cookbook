package pl.kamil.chefscookbook.shared.exceptions;

public class NotAuthorizedException extends Throwable {

    public static final String NOT_AUTHORIZED_MESSAGE = "Nie masz uprawnień do tego przedmiotu";

    public NotAuthorizedException() {
        super(NOT_AUTHORIZED_MESSAGE);
    }
}
