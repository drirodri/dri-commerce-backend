package dri.commerce.user.domain.exception;

public class WeakPasswordException extends RuntimeException {
    
    public WeakPasswordException(String message) {
        super(message);
    }
}