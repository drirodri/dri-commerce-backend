package dri.commerce.product.application.usecase;

import dri.commerce.product.domain.entity.ProductDomain;
import dri.commerce.product.domain.exception.ProductNotFoundException;
import dri.commerce.product.domain.repository.ProductRepository;
import dri.commerce.product.domain.valueobject.ProductId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FindProductByIdUseCase {

    @Inject
    ProductRepository productRepository;

    public ProductDomain execute(String productId) {
        ProductId id = ProductId.from(productId);
        return productRepository.findById(id)
                .orElseThrow(() -> ProductNotFoundException.withId(productId));
    }
}
