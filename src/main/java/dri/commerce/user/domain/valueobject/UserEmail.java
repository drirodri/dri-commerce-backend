package dri.commerce.user.domain.valueobject;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserEmail(@NotBlank @Email String value) {

    public UserEmail {
        value = value != null ? value.trim().toLowerCase() : null;
    }

    public String getDomain() {
        return value.substring(value.indexOf('@') + 1);
    }
}
