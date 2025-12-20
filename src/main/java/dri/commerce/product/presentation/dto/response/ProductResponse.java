package dri.commerce.product.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dri.commerce.product.domain.entity.ProductDomain;

public record ProductResponse(
        String id,
        String title,
        BigDecimal price,
        String thumbnail,
        Integer availableQuantity,
        String condition,
        Long categoryId,
        String sellerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean active
) {

    public static ProductResponse fromDomain(ProductDomain product) {
        return new ProductResponse(
                product.id().value(),
                product.title(),
                product.price().value(),
                product.thumbnail(),
                product.availableQuantity(),
                product.condition(),
                product.categoryId(),
                product.sellerId().value(),
                product.createdAt(),
                product.updatedAt(),
                product.active()
        );
    }
}
