package dri.commerce.product.infrastructure.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_product_seller", columnList = "seller_id"),
    @Index(name = "idx_product_category", columnList = "category_id"),
    @Index(name = "idx_product_active", columnList = "active")
})
public class ProductEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;

    @Column(nullable = false, length = 500)
    public String title;

    @Column(nullable = false, precision = 10, scale = 2)
    public BigDecimal price;

    @Column(length = 500)
    public String thumbnail;

    @Column(name = "available_quantity", nullable = false)
    public Integer availableQuantity = 0;

    @Column(length = 50)
    public String condition;

    @Column(name = "category_id")
    public Long categoryId;

    @Column(name = "seller_id", nullable = false, length = 36)
    public String sellerId;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @Column(nullable = false)
    public boolean active = true;

    public ProductEntity() {
    }

    public ProductEntity(String id, String title, BigDecimal price, String thumbnail,
                         Integer availableQuantity, String condition, Long categoryId,
                         String sellerId, LocalDateTime createdAt, LocalDateTime updatedAt,
                         boolean active) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.thumbnail = thumbnail;
        this.availableQuantity = availableQuantity;
        this.condition = condition;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
    }
}
