package dri.commerce.category.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCategoryRequest(
        @NotBlank(message = "O nome da categoria é obrigatório")
        @Size(max = 100, message = "O nome da categoria não pode exceder 100 caracteres")
        String name,

        @Size(max = 1000, message = "A descrição não pode exceder 1000 caracteres")
        String description
) {}
