package dri.commerce.category.domain.repository;

import java.util.List;
import java.util.Optional;

import dri.commerce.category.domain.entity.CategoryDomain;
import dri.commerce.category.domain.valueobject.CategoryId;

public interface CategoryRepository {

    CategoryDomain save(CategoryDomain category);

    CategoryDomain update(CategoryDomain category);

    Optional<CategoryDomain> findById(CategoryId id);

    List<CategoryDomain> findAll();

    boolean deleteById(CategoryId id);

    boolean existsById(CategoryId id);

    long count();
}
