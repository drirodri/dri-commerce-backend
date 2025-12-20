package dri.commerce.product.application.usecase;

import java.math.BigDecimal;

import dri.commerce.product.domain.entity.ProductDomain;
import dri.commerce.product.domain.exception.ProductNotFoundException;
import dri.commerce.product.domain.repository.ProductRepository;
import dri.commerce.product.domain.valueobject.Price;
import dri.commerce.product.domain.valueobject.ProductId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpdateProductUseCase {

    @Inject
    ProductRepository productRepository;

    public ProductDomain execute(UpdateProductCommand command) {
        ProductId productId = ProductId.from(command.productId());

        ProductDomain existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> ProductNotFoundException.withId(command.productId()));

        String title = command.title() != null ? command.title() : existingProduct.title();
        Price price = command.price() != null ? Price.from(command.price()) : existingProduct.price();
        String thumbnail = command.thumbnail() != null ? command.thumbnail() : existingProduct.thumbnail();
        String condition = command.condition() != null ? command.condition() : existingProduct.condition();
        Long categoryId = command.categoryId() != null ? command.categoryId() : existingProduct.categoryId();

        ProductDomain updatedProduct = existingProduct.updateInfo(title, price, thumbnail, condition, categoryId);

        if (command.availableQuantity() != null && !command.availableQuantity().equals(existingProduct.availableQuantity())) {
            updatedProduct = updatedProduct.updateQuantity(command.availableQuantity());
        }

        return productRepository.update(updatedProduct);
    }

    public record UpdateProductCommand(
            String productId,
            String title,
            BigDecimal price,
            String thumbnail,
            Integer availableQuantity,
            String condition,
            Long categoryId
    ) {
    }
}
