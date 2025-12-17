package dri.commerce.user.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.ws.rs.QueryParam;

public class PageRequest {

    @QueryParam("page")
    @Min(value = 0, message = "Page must be greater than or equal to 0")
    private Integer page;

    @QueryParam("pageSize")
    @Min(value = 1, message = "Page size must be greater than 0")
    private Integer pageSize;

    public PageRequest() {
    }

    public Integer page() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer pageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public int getSkip() {
        int p = page != null ? page : 0;
        int ps = pageSize != null ? pageSize : 20;
        return p * ps;
    }

    public int getLimit() {
        return pageSize != null ? pageSize : 20;
    }
}