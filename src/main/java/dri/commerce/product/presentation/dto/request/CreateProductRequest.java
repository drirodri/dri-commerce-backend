package dri.commerce.product.presentation.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(
        @NotBlank(message = "Title cannot be blank")
        @Size(max = 500, message = "Title cannot exceed 500 characters")
        String title,

        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
        BigDecimal price,

        @Size(max = 500, message = "Thumbnail URL cannot exceed 500 characters")
        String thumbnail,

        @NotNull(message = "Available quantity cannot be null")
        @Min(value = 0, message = "Available quantity cannot be negative")
        Integer availableQuantity,

        @Size(max = 50, message = "Condition cannot exceed 50 characters")
        String condition,

        @Size(max = 100, message = "Category ID cannot exceed 100 characters")
        String categoryId
) {
}
