package dri.commerce.user.domain.valueobject;

import jakarta.validation.constraints.NotBlank;

public record UserId(@NotBlank String value) {

    public static UserId from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be blank");
        }

        if (!value.matches("^[0-9a-fA-F]{24}$")) {
            throw new IllegalArgumentException("Invalid User ID format: " + value);
        }

        return new UserId(value);
    }

}

