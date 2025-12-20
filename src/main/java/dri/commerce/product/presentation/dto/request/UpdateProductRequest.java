package dri.commerce.product.presentation.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateProductRequest(
        @Size(max = 500, message = "Title cannot exceed 500 characters")
        String title,

        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
        BigDecimal price,

        @Size(max = 500, message = "Thumbnail URL cannot exceed 500 characters")
        String thumbnail,

        @Min(value = 0, message = "Available quantity cannot be negative")
        Integer availableQuantity,

        @Size(max = 50, message = "Condition cannot exceed 50 characters")
        String condition,

        Long categoryId
) {
}
