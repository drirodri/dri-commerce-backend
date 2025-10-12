package dri.commerce.auth.presentation.dto;

import dri.commerce.user.domain.enums.Role;

public record MeResponse(
    String id,
    String name,
    String email,
    Role role
) {
    
}
