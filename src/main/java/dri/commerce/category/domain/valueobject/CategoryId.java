package dri.commerce.category.domain.valueobject;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CategoryId(
        @NotNull(message = "Category ID cannot be null")
        @Positive(message = "Category ID must be positive")
        Long value
) {
    public CategoryId {
        if (value != null && value <= 0) {
            throw new IllegalArgumentException("Category ID must be positive");
        }
    }
}
