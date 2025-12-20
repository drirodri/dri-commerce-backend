package dri.commerce.category.domain.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long id) {
        super("Categoria não encontrada com ID: " + id);
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
