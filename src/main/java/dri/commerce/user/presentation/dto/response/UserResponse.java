package dri.commerce.user.presentation.dto.response;

import java.time.LocalDateTime;

import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.enums.Role;

public record UserResponse(
        String id,
        String name,
        String email,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean active
) {

    public static UserResponse fromDomain(UserDomain user) {
        return new UserResponse(
                user.id().value(),
                user.name(),
                user.email().value(),
                user.role(),
                user.createdAt(),
                user.updatedAt(),
                user.active()
        );
    }
}
