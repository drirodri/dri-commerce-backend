package dri.commerce.category.application.usecase;

import dri.commerce.category.domain.entity.CategoryDomain;
import dri.commerce.category.domain.exception.CategoryNotFoundException;
import dri.commerce.category.domain.repository.CategoryRepository;
import dri.commerce.category.domain.valueobject.CategoryId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpdateCategoryUseCase {

    @Inject
    CategoryRepository categoryRepository;

    public CategoryDomain execute(UpdateCategoryCommand command) {
        CategoryId categoryId = new CategoryId(command.id());

        CategoryDomain existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(command.id()));

        CategoryDomain updatedCategory = existingCategory.updateInfo(
                command.name(),
                command.description()
        );

        return categoryRepository.update(updatedCategory);
    }

    public record UpdateCategoryCommand(
            Long id,
            String name,
            String description
    ) {}
}
