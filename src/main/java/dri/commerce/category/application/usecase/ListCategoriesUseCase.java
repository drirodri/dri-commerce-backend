package dri.commerce.category.application.usecase;

import java.util.List;

import dri.commerce.category.domain.entity.CategoryDomain;
import dri.commerce.category.domain.repository.CategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ListCategoriesUseCase {

    @Inject
    CategoryRepository categoryRepository;

    public List<CategoryDomain> execute() {
        return categoryRepository.findAll();
    }
}
