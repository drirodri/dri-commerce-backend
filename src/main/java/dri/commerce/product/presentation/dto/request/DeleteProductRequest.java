package dri.commerce.product.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request para deletar produto permanentemente
 * Requer confirmação do nome do produto para evitar deleções acidentais
 */
public record DeleteProductRequest(
    @NotBlank(message = "Nome de confirmação é obrigatório")
    String confirmationName
) {}
