package dri.commerce.auth.domain.exception;

/**
 * Exception lancada quando um cliente excede o limite de tentativas de login
 * Deve resultar em HTTP 429 Too Many Requests
 */
public class RateLimitExceededException extends RuntimeException {
    
    private final int remainingAttempts;
    private final long minutesUntilReset;

    public RateLimitExceededException(int remainingAttempts, long minutesUntilReset) {
        super(String.format(
            "Limite de tentativas de login excedido. Tente novamente em %d minutos.",
            minutesUntilReset
        ));
        this.remainingAttempts = remainingAttempts;
        this.minutesUntilReset = minutesUntilReset;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public long getMinutesUntilReset() {
        return minutesUntilReset;
    }
}
