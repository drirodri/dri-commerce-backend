package dri.commerce.product.domain.service;

import dri.commerce.product.domain.entity.ProductDomain;
import dri.commerce.product.domain.exception.ProductAccessDeniedException;
import dri.commerce.product.domain.exception.ProductNameMismatchException;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Serviço de domínio para validações de autorização relacionadas a produtos.
 * Centraliza regras de negócio de acesso que podem ser reutilizadas em múltiplos use cases.
 */
@ApplicationScoped
public class ProductAuthorizationService {

    /**
     * Valida se o usuário tem permissão para modificar o produto.
     * ADMIN pode modificar qualquer produto.
     * SELLER só pode modificar seus próprios produtos.
     * 
     * @param product Produto a ser modificado
     * @param userId ID do usuário que está tentando modificar
     * @param isAdmin Se o usuário é admin
     * @throws ProductAccessDeniedException se o usuário não tiver permissão
     */
    public void validateOwnership(ProductDomain product, String userId, boolean isAdmin) {
        if (isAdmin) {
            return;
        }
        
        if (!product.sellerId().value().equals(userId)) {
            throw new ProductAccessDeniedException(product.id().value());
        }
    }

    /**
     * Valida se o nome de confirmação corresponde ao título do produto.
     * Usado para operações destrutivas como delete.
     * 
     * @param product Produto a ser deletado
     * @param confirmationName Nome digitado para confirmação
     * @throws ProductNameMismatchException se o nome não corresponder
     */
    public void validateNameConfirmation(ProductDomain product, String confirmationName) {
        if (!product.title().equals(confirmationName)) {
            throw new ProductNameMismatchException();
        }
    }
}
