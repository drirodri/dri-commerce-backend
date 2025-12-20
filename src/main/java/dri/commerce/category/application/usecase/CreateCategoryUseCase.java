package dri.commerce.category.application.usecase;

import dri.commerce.category.domain.entity.CategoryDomain;
import dri.commerce.category.domain.repository.CategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateCategoryUseCase {

    @Inject
    CategoryRepository categoryRepository;

    public CategoryDomain execute(CreateCategoryCommand command) {
        CategoryDomain category = CategoryDomain.create(
                command.name(),
                command.description()
        );

        return categoryRepository.save(category);
    }

    public record CreateCategoryCommand(
            String name,
            String description
    ) {}
}
