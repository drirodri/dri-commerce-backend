package dri.commerce.user.domain.valueobject;

import dri.commerce.user.domain.exception.WeakPasswordException;
import jakarta.validation.constraints.NotBlank;

public record UserPassword(
        @NotBlank(message = "Password cannot be blank")
        String value
) {

    public UserPassword {
        if (value != null && !isHash(value) && !isStrongPassword(value)) {
            throw new WeakPasswordException(
                    "Password must be at least 12 characters with uppercase, lowercase, number and special character"
            );
        }
    }

    public static void validateStrength(String password) {
        if (password == null || password.isBlank()) {
            throw new WeakPasswordException("Password cannot be blank");
        }
        
        if (!isStrongPassword(password)) {
            throw new WeakPasswordException(
                    "Password must be at least 12 characters with uppercase, lowercase, number and special character"
            );
        }
    }

    private static boolean isHash(String password) {
        return password != null && password.startsWith("$2a$");
    }

    private static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 12) {
            return false;
        }

        return password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[0-9].*") &&
                password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
    }
}