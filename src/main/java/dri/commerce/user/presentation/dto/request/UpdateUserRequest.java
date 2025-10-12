package dri.commerce.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @Email(message = "Invalid email format")
        String email,

        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password
) {
}
