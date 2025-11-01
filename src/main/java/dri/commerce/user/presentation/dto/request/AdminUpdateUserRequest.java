package dri.commerce.user.presentation.dto.request;

import dri.commerce.user.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record AdminUpdateUserRequest(

        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @Email(message = "Invalid email format")
        String email,

        Role role,

        Boolean active
) {
}
