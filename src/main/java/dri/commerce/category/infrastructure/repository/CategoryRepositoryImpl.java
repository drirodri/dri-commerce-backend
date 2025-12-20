package dri.commerce.category.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dri.commerce.category.domain.entity.CategoryDomain;
import dri.commerce.category.domain.repository.CategoryRepository;
import dri.commerce.category.domain.valueobject.CategoryId;
import dri.commerce.category.infrastructure.entity.CategoryEntity;
import dri.commerce.category.infrastructure.mapper.CategoryMapper;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CategoryRepositoryImpl implements CategoryRepository {

    @Inject
    CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDomain save(CategoryDomain category) {
        CategoryEntity entity = categoryMapper.toInfrastructure(category);
        entity.persist();
        return categoryMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public CategoryDomain update(CategoryDomain category) {
        CategoryEntity entity = CategoryEntity.findById(category.id().value());
        if (entity == null) {
            return null;
        }

        entity.name = category.name();
        entity.description = category.description();
        entity.updatedAt = category.updatedAt();

        return categoryMapper.toDomain(entity);
    }

    @Override
    public Optional<CategoryDomain> findById(CategoryId id) {
        CategoryEntity entity = CategoryEntity.findById(id.value());
        return Optional.ofNullable(categoryMapper.toDomain(entity));
    }

    @Override
    public List<CategoryDomain> findAll() {
        return CategoryEntity.<CategoryEntity>listAll(Sort.ascending("name"))
                .stream()
                .map(categoryMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteById(CategoryId id) {
        return CategoryEntity.deleteById(id.value());
    }

    @Override
    public boolean existsById(CategoryId id) {
        return CategoryEntity.findById(id.value()) != null;
    }

    @Override
    public long count() {
        return CategoryEntity.count();
    }
}
