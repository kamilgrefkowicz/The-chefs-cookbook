package pl.kamil.chefscookbook.shared.exceptions;

public class NotAuthorizedException extends Throwable {

    public NotAuthorizedException() {
        super("Nie masz uprawnień do tego przedmiotu");
    }
}
