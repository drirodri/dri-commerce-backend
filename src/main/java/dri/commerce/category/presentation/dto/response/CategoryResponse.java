package dri.commerce.category.presentation.dto.response;

import java.time.LocalDateTime;

import dri.commerce.category.domain.entity.CategoryDomain;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CategoryResponse fromDomain(CategoryDomain domain) {
        return new CategoryResponse(
                domain.id() != null ? domain.id().value() : null,
                domain.name(),
                domain.description(),
                domain.createdAt(),
                domain.updatedAt()
        );
    }
}
