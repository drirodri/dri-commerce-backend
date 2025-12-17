package dri.commerce.product.domain.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public static ProductNotFoundException withId(String id) {
        return new ProductNotFoundException("Product not found with ID: " + id);
    }
}
