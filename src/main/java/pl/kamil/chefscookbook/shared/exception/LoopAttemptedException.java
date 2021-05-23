package pl.kamil.chefscookbook.shared.exception;

public class LoopAttemptedException extends Throwable{
    public LoopAttemptedException(String message) {
        super(message);
    }
}
