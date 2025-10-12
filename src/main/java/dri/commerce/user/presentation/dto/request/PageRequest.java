package dri.commerce.user.presentation.dto.request;

import jakarta.validation.constraints.Min;

public record PageRequest(

        @Min(value = 1, message = "Page must be greater than 0")
        Integer page,

        @Min(value = 1, message = "Page size must be greater than 0")
        Integer pageSize
) {

    public PageRequest {
        page = page != null ? page : 1;
        pageSize = pageSize != null ? pageSize : 10;
    }

    public int getSkip() {
        return (page - 1) * pageSize;
    }

    public int getLimit() {
        return pageSize;
    }
}