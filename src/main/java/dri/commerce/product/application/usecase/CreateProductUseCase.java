package dri.commerce.product.application.usecase;

import java.math.BigDecimal;

import dri.commerce.product.domain.entity.ProductDomain;
import dri.commerce.product.domain.repository.ProductRepository;
import dri.commerce.product.domain.valueobject.Price;
import dri.commerce.product.domain.valueobject.ProductId;
import dri.commerce.user.domain.valueobject.UserId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class CreateProductUseCase {

    @Inject
    ProductRepository productRepository;

    public ProductDomain execute(@NotNull @Valid CreateProductCommand command) {
        Price price = Price.from(command.price());
        UserId sellerId = UserId.from(command.sellerId());

        ProductDomain product = ProductDomain.create(
                command.title(),
                price,
                command.thumbnail(),
                command.availableQuantity(),
                command.condition(),
                command.categoryId(),
                sellerId
        );

        ProductDomain savedProduct = productRepository.save(product);
        return savedProduct.withId(ProductId.from(savedProduct.id().value()));
    }

    public record CreateProductCommand(
            String title,
            BigDecimal price,
            String thumbnail,
            Integer availableQuantity,
            String condition,
            String categoryId,
            String sellerId
    ) {
    }
}
