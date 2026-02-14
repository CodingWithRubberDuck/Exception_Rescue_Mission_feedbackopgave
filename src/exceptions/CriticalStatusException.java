package exceptions;

public class CriticalStatusException extends RuntimeException {
    public CriticalStatusException(String message) {
        super(message);
    }
}
