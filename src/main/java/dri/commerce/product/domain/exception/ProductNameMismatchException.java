package dri.commerce.product.domain.exception;

public class ProductNameMismatchException extends RuntimeException {
    
    public ProductNameMismatchException() {
        super("O nome de confirmação não corresponde ao título do produto");
    }
}
