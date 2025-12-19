package dri.commerce.product.domain.exception;

public class ProductAccessDeniedException extends RuntimeException {
    
    public ProductAccessDeniedException(String productId) {
        super("Você não tem permissão para modificar o produto: " + productId);
    }
    
    public static ProductAccessDeniedException forProduct(String productId) {
        return new ProductAccessDeniedException(productId);
    }
}
