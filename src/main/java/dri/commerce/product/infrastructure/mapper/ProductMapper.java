package dri.commerce.product.infrastructure.mapper;

import dri.commerce.product.domain.entity.ProductDomain;
import dri.commerce.product.domain.valueobject.Price;
import dri.commerce.product.domain.valueobject.ProductId;
import dri.commerce.product.infrastructure.entity.ProductEntity;
import dri.commerce.user.domain.valueobject.UserId;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductMapper {

    public ProductEntity toInfrastructure(ProductDomain domainProduct) {
        if (domainProduct == null) {
            return null;
        }

        return new ProductEntity(
                domainProduct.id() != null ? domainProduct.id().value() : null,
                domainProduct.title(),
                domainProduct.price().value(),
                domainProduct.thumbnail(),
                domainProduct.availableQuantity(),
                domainProduct.condition(),
                domainProduct.categoryId(),
                domainProduct.sellerId().value(),
                domainProduct.createdAt(),
                domainProduct.updatedAt(),
                domainProduct.active()
        );
    }

    public ProductDomain toDomain(ProductEntity infraProduct) {
        if (infraProduct == null) {
            return null;
        }

        ProductId id = infraProduct.id != null ? ProductId.from(infraProduct.id) : null;
        Price price = Price.from(infraProduct.price);
        UserId sellerId = UserId.from(infraProduct.sellerId);

        return ProductDomain.restore(
                id,
                infraProduct.title,
                price,
                infraProduct.thumbnail,
                infraProduct.availableQuantity,
                infraProduct.condition,
                infraProduct.categoryId,
                sellerId,
                infraProduct.createdAt,
                infraProduct.updatedAt,
                infraProduct.active
        );
    }
}
