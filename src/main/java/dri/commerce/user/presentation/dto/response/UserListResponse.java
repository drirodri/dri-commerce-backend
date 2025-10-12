package dri.commerce.user.presentation.dto.response;

import java.util.List;

import dri.commerce.user.domain.entity.Page;

public record UserListResponse(
        List<UserResponse> users,
        long total,
        int page,
        int pageSize,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {

    public static UserListResponse of(List<UserResponse> users) {
        return new UserListResponse(
                users,
                users.size(),
                1,
                users.size(),
                1,
                false,
                false
        );
    }

    public static UserListResponse fromPage(Page<UserResponse> page) {
        return new UserListResponse(
                page.content(),
                page.total(),
                page.page(),
                page.pageSize(),
                page.totalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
