package exceptions;

public class UnknownSituationException extends RuntimeException {
    public UnknownSituationException(String message) {
        super(message);
    }
}
