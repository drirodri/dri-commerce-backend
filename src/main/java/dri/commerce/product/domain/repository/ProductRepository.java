package dri.commerce.product.domain.repository;

import java.util.List;
import java.util.Optional;

import dri.commerce.product.domain.entity.ProductDomain;
import dri.commerce.product.domain.valueobject.ProductId;
import dri.commerce.user.domain.entity.Page;
import dri.commerce.user.domain.valueobject.UserId;

public interface ProductRepository {

    ProductDomain save(ProductDomain product);

    ProductDomain update(ProductDomain product);

    Optional<ProductDomain> findById(ProductId id);

    List<ProductDomain> findAllActive();

    List<ProductDomain> getAllProducts();

    Page<ProductDomain> findAll(int page, int pageSize);

    List<ProductDomain> findBySellerId(UserId sellerId);

    List<ProductDomain> findByCategoryId(String categoryId);

    List<ProductDomain> findByTitleContaining(String title);

    boolean deleteById(ProductId id);

    long countActive();

    long count();

    long countBySellerId(UserId sellerId);
}
