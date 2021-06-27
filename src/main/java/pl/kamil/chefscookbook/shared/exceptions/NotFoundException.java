package pl.kamil.chefscookbook.shared.exceptions;

public class NotFoundException extends Throwable {

    public NotFoundException() {
        super("Nie znaleźliśmy tego, czego szukasz :(");
    }
}
