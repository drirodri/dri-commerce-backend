package dri.commerce.user.infrastructure.entity;

import java.time.LocalDateTime;

public record UserEntity(
        String id,
        String name,
        String email,
        String password,
        Integer role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean active
) {
}
