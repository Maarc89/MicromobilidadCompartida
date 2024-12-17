package exceptions;

public class InvalidLongLattException extends RuntimeException {
    public InvalidLongLattException(String message) {
        super(message);
    }

    public InvalidLongLattException(String message, Throwable cause) {
        super(message, cause);
    }
}
