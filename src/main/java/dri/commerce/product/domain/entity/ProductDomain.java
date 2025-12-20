package dri.commerce.product.domain.entity;

import java.time.LocalDateTime;

import dri.commerce.product.domain.valueobject.Price;
import dri.commerce.product.domain.valueobject.ProductId;
import dri.commerce.user.domain.valueobject.UserId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record ProductDomain(
        @Valid
        ProductId id,

        @NotBlank(message = "Title cannot be blank")
        @Size(max = 500, message = "Title cannot exceed 500 characters")
        String title,

        @Valid
        @NotNull(message = "Price cannot be null")
        Price price,

        @Size(max = 500, message = "Thumbnail URL cannot exceed 500 characters")
        String thumbnail,

        @NotNull(message = "Available quantity cannot be null")
        @Min(value = 0, message = "Available quantity cannot be negative")
        Integer availableQuantity,

        @Size(max = 50, message = "Condition cannot exceed 50 characters")
        String condition,

        Long categoryId,

        @Valid
        @NotNull(message = "Seller ID cannot be null")
        UserId sellerId,

        @NotNull(message = "Creation date cannot be null")
        @PastOrPresent(message = "Creation date must be in the past or present")
        LocalDateTime createdAt,

        @PastOrPresent(message = "Update date must be in the past or present")
        LocalDateTime updatedAt,

        @NotNull(message = "Active status cannot be null")
        Boolean active
) {

    public ProductDomain {
        title = title != null ? title.trim() : null;
        condition = condition != null ? condition.trim() : null;
    }

    public static ProductDomain create(String title, Price price, String thumbnail,
                                       Integer availableQuantity, String condition,
                                       Long categoryId, UserId sellerId) {
        LocalDateTime now = LocalDateTime.now();
        return new ProductDomain(null, title, price, thumbnail, availableQuantity,
                condition, categoryId, sellerId, now, now, true);
    }

    public static ProductDomain restore(ProductId id, String title, Price price, String thumbnail,
                                        Integer availableQuantity, String condition, Long categoryId,
                                        UserId sellerId, LocalDateTime createdAt,
                                        LocalDateTime updatedAt, Boolean active) {
        return new ProductDomain(id, title, price, thumbnail, availableQuantity,
                condition, categoryId, sellerId, createdAt, updatedAt, active);
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isInStock() {
        return this.availableQuantity > 0;
    }

    public ProductDomain updateInfo(String newTitle, Price newPrice, String newThumbnail,
                                    String newCondition, Long newCategoryId) {
        return new ProductDomain(
                this.id,
                newTitle,
                newPrice,
                newThumbnail,
                this.availableQuantity,
                newCondition,
                newCategoryId,
                this.sellerId,
                this.createdAt,
                LocalDateTime.now(),
                this.active
        );
    }

    public ProductDomain updateQuantity(Integer newQuantity) {
        return new ProductDomain(
                this.id,
                this.title,
                this.price,
                this.thumbnail,
                newQuantity,
                this.condition,
                this.categoryId,
                this.sellerId,
                this.createdAt,
                LocalDateTime.now(),
                this.active
        );
    }

    public ProductDomain decreaseQuantity(Integer amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.availableQuantity < amount) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        return updateQuantity(this.availableQuantity - amount);
    }

    public ProductDomain increaseQuantity(Integer amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        return updateQuantity(this.availableQuantity + amount);
    }

    public ProductDomain activate() {
        return new ProductDomain(
                this.id,
                this.title,
                this.price,
                this.thumbnail,
                this.availableQuantity,
                this.condition,
                this.categoryId,
                this.sellerId,
                this.createdAt,
                LocalDateTime.now(),
                true
        );
    }

    public ProductDomain deactivate() {
        return new ProductDomain(
                this.id,
                this.title,
                this.price,
                this.thumbnail,
                this.availableQuantity,
                this.condition,
                this.categoryId,
                this.sellerId,
                this.createdAt,
                LocalDateTime.now(),
                false
        );
    }

    public ProductDomain withId(ProductId newId) {
        return new ProductDomain(
                newId,
                this.title,
                this.price,
                this.thumbnail,
                this.availableQuantity,
                this.condition,
                this.categoryId,
                this.sellerId,
                this.createdAt,
                this.updatedAt,
                this.active
        );
    }
}
