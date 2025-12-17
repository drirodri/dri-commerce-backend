package dri.commerce.product.domain.valueobject;

import jakarta.validation.constraints.NotBlank;

public record ProductId(@NotBlank String value) {

    public static ProductId from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be blank");
        }

        if (!value.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            throw new IllegalArgumentException("Invalid Product ID format: " + value);
        }

        return new ProductId(value);
    }
}
