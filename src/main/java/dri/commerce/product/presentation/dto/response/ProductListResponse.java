package dri.commerce.product.presentation.dto.response;

import java.util.List;

import dri.commerce.user.domain.entity.Page;

public record ProductListResponse(
        List<ProductResponse> products,
        int currentPage,
        int pageSize,
        int totalPages,
        long totalElements
) {

    public static ProductListResponse fromPage(Page<ProductResponse> page) {
        return new ProductListResponse(
                page.content(),
                page.page(),
                page.pageSize(),
                page.totalPages(),
                page.total()
        );
    }
}
