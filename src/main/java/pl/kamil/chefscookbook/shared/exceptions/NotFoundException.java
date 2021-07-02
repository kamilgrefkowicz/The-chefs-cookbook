package pl.kamil.chefscookbook.shared.exceptions;

public class NotFoundException extends Throwable {

    public static final String NOT_FOUND_MESSAGE = "Nie znaleźliśmy tego, czego szukasz :(";

    public NotFoundException() {
        super(NOT_FOUND_MESSAGE);
    }
}
