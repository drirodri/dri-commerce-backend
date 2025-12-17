package dri.commerce.product.domain.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }

    public static InsufficientStockException forProduct(String productId, int requested, int available) {
        return new InsufficientStockException(
                String.format("Insufficient stock for product %s. Requested: %d, Available: %d",
                        productId, requested, available)
        );
    }
}
