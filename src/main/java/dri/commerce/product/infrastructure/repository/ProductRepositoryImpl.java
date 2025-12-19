package dri.commerce.product.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dri.commerce.product.domain.entity.ProductDomain;
import dri.commerce.product.domain.repository.ProductRepository;
import dri.commerce.product.domain.valueobject.ProductId;
import dri.commerce.product.infrastructure.entity.ProductEntity;
import dri.commerce.product.infrastructure.mapper.ProductMapper;
import dri.commerce.user.domain.entity.Page;
import dri.commerce.user.domain.valueobject.UserId;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProductRepositoryImpl implements ProductRepository {

    @Inject
    ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDomain save(ProductDomain product) {
        ProductEntity entity = productMapper.toInfrastructure(product);
        entity.persist();
        return productMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public ProductDomain update(ProductDomain product) {
        ProductEntity entity = ProductEntity.findById(product.id().value());
        if (entity == null) {
            return null;
        }

        entity.title = product.title();
        entity.price = product.price().value();
        entity.thumbnail = product.thumbnail();
        entity.availableQuantity = product.availableQuantity();
        entity.condition = product.condition();
        entity.categoryId = product.categoryId();
        entity.updatedAt = product.updatedAt();
        entity.active = product.active();

        return productMapper.toDomain(entity);
    }

    @Override
    public Optional<ProductDomain> findById(ProductId id) {
        ProductEntity entity = ProductEntity.findById(id.value());
        return Optional.ofNullable(productMapper.toDomain(entity));
    }

    @Override
    public List<ProductDomain> findAllActive() {
        return ProductEntity.<ProductEntity>list("active = true", Sort.descending("createdAt"))
                .stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDomain> getAllProducts() {
        return ProductEntity.<ProductEntity>listAll(Sort.descending("createdAt"))
                .stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDomain> findAll(int page, int pageSize) {
        PanacheQuery<ProductEntity> query = ProductEntity.findAll(Sort.descending("createdAt"));
        query.page(page - 1, pageSize); // API é 1-indexed, Panache é 0-indexed

        List<ProductDomain> products = query.list()
                .stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());

    long totalElements = ProductEntity.count();
    int totalPages = (int) Math.ceil((double) totalElements / pageSize);

    return new Page<>(
        products,
        totalElements,
        page,
        pageSize,
        totalPages
    );
    }

    @Override
    public List<ProductDomain> findBySellerId(UserId sellerId) {
        return ProductEntity.<ProductEntity>list("sellerId = ?1", Sort.descending("createdAt"), sellerId.value())
                .stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDomain> findByCategoryId(String categoryId) {
        return ProductEntity.<ProductEntity>list("categoryId = ?1 and active = true",
                        Sort.descending("createdAt"), categoryId)
                .stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDomain> findByTitleContaining(String title) {
        return ProductEntity.<ProductEntity>list("LOWER(title) LIKE ?1 and active = true",
                        Sort.descending("createdAt"), "%" + title.toLowerCase() + "%")
                .stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteById(ProductId id) {
        return ProductEntity.deleteById(id.value());
    }

    @Override
    public long countActive() {
        return ProductEntity.count("active = true");
    }

    @Override
    public long count() {
        return ProductEntity.count();
    }

    @Override
    public long countBySellerId(UserId sellerId) {
        return ProductEntity.count("sellerId = ?1", sellerId.value());
    }
}
