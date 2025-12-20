package dri.commerce.category.domain.entity;

import java.time.LocalDateTime;

import dri.commerce.category.domain.valueobject.CategoryId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record CategoryDomain(
        @Valid
        CategoryId id,

        @NotBlank(message = "Category name cannot be blank")
        @Size(max = 100, message = "Category name cannot exceed 100 characters")
        String name,

        @Size(max = 1000, message = "Category description cannot exceed 1000 characters")
        String description,

        @PastOrPresent(message = "Creation date must be in the past or present")
        LocalDateTime createdAt,

        @PastOrPresent(message = "Update date must be in the past or present")
        LocalDateTime updatedAt
) {

    public CategoryDomain {
        name = name != null ? name.trim() : null;
        description = description != null ? description.trim() : null;
    }

    public static CategoryDomain create(String name, String description) {
        LocalDateTime now = LocalDateTime.now();
        return new CategoryDomain(null, name, description, now, now);
    }

    public static CategoryDomain restore(CategoryId id, String name, String description,
                                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new CategoryDomain(id, name, description, createdAt, updatedAt);
    }

    public CategoryDomain updateInfo(String newName, String newDescription) {
        return new CategoryDomain(
                this.id,
                newName,
                newDescription,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    public CategoryDomain withId(CategoryId newId) {
        return new CategoryDomain(
                newId,
                this.name,
                this.description,
                this.createdAt,
                this.updatedAt
        );
    }
}
