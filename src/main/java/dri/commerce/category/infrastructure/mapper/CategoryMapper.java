package dri.commerce.category.infrastructure.mapper;

import dri.commerce.category.domain.entity.CategoryDomain;
import dri.commerce.category.domain.valueobject.CategoryId;
import dri.commerce.category.infrastructure.entity.CategoryEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryMapper {

    public CategoryDomain toDomain(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return CategoryDomain.restore(
                new CategoryId(entity.id),
                entity.name,
                entity.description,
                entity.createdAt,
                entity.updatedAt
        );
    }

    public CategoryEntity toInfrastructure(CategoryDomain domain) {
        if (domain == null) {
            return null;
        }

        return new CategoryEntity(
                domain.id() != null ? domain.id().value() : null,
                domain.name(),
                domain.description(),
                domain.createdAt(),
                domain.updatedAt()
        );
    }
}
