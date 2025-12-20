package dri.commerce.category.application.usecase;

import dri.commerce.category.domain.entity.CategoryDomain;
import dri.commerce.category.domain.exception.CategoryNotFoundException;
import dri.commerce.category.domain.repository.CategoryRepository;
import dri.commerce.category.domain.valueobject.CategoryId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FindCategoryByIdUseCase {

    @Inject
    CategoryRepository categoryRepository;

    public CategoryDomain execute(Long id) {
        CategoryId categoryId = new CategoryId(id);

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
