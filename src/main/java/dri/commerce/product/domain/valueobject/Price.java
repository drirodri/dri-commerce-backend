package dri.commerce.product.domain.valueobject;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record Price(
        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
        BigDecimal value
) {

    public static Price from(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        return new Price(value);
    }

    public static Price from(double value) {
        return from(BigDecimal.valueOf(value));
    }

    public boolean isGreaterThan(Price other) {
        return this.value.compareTo(other.value) > 0;
    }

    public boolean isLessThan(Price other) {
        return this.value.compareTo(other.value) < 0;
    }
}
