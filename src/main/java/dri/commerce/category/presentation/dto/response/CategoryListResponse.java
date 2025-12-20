package dri.commerce.category.presentation.dto.response;

import java.util.List;

public record CategoryListResponse(
        List<CategoryResponse> categories,
        long total
) {
    public static CategoryListResponse fromList(List<CategoryResponse> categories) {
        return new CategoryListResponse(categories, categories.size());
    }
}
