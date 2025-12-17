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

    public Page<ProductDomain> execute(int page, int pageSize) {
        return productRepository.findAll(page, pageSize);
    }
}
