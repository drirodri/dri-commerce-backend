package dri.commerce.product.application.usecase;

import dri.commerce.product.domain.entity.ProductDomain;
import dri.commerce.product.domain.exception.ProductNotFoundException;
import dri.commerce.product.domain.repository.ProductRepository;
import dri.commerce.product.domain.service.ProductAuthorizationService;
import dri.commerce.product.domain.valueobject.ProductId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeactivateProductUseCase {

    @Inject
    ProductRepository productRepository;

    @Inject
    ProductAuthorizationService authorizationService;

    public ProductDomain execute(String productId) {
        ProductId id = ProductId.from(productId);

        ProductDomain product = productRepository.findById(id)
                .orElseThrow(() -> ProductNotFoundException.withId(productId));

        ProductDomain deactivatedProduct = product.deactivate();
        return productRepository.update(deactivatedProduct);
    }

    public void executeHardDelete(HardDeleteCommand command) {
        ProductId id = ProductId.from(command.productId());
        
        ProductDomain product = productRepository.findById(id)
                .orElseThrow(() -> ProductNotFoundException.withId(command.productId()));
        
        authorizationService.validateOwnership(product, command.userId(), command.isAdmin());
        authorizationService.validateNameConfirmation(product, command.confirmationName());
        
        boolean deleted = productRepository.deleteById(id);

        if (!deleted) {
            throw ProductNotFoundException.withId(command.productId());
        }
    }

    public record HardDeleteCommand(
        String productId,
        String confirmationName,
        String userId,
        boolean isAdmin
    ) {}
}
