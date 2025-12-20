package dri.commerce.category.application.usecase;

import dri.commerce.category.domain.exception.CategoryNotFoundException;
import dri.commerce.category.domain.repository.CategoryRepository;
import dri.commerce.category.domain.valueobject.CategoryId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeleteCategoryUseCase {

    @Inject
    CategoryRepository categoryRepository;

    public void execute(Long id) {
        CategoryId categoryId = new CategoryId(id);

        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(id);
        }

        categoryRepository.deleteById(categoryId);
    }
}
