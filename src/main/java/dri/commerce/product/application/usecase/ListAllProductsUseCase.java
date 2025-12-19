package dri.commerce.product.application.usecase;

import dri.commerce.product.domain.entity.ProductDomain;
import dri.commerce.product.domain.repository.ProductRepository;
import dri.commerce.user.domain.entity.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ListAllProductsUseCase {

    @Inject
    ProductRepository productRepository;

    public Page<ProductDomain> executePublic(int page, int pageSize) {
        return productRepository.findAllActive(page, pageSize);
    }

    public Page<ProductDomain> executeBySeller(int page, int pageSize, String sellerId) {
        return productRepository.findBySellerId(page, pageSize, sellerId);
    }

    public Page<ProductDomain> executeAdmin(int page, int pageSize) {
        return productRepository.findAllIncludingInactive(page, pageSize);
    }
}
