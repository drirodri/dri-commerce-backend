package dri.commerce.user.domain.entity;

import java.time.LocalDateTime;

import dri.commerce.user.domain.enums.Role;
import dri.commerce.user.domain.valueobject.UserEmail;
import dri.commerce.user.domain.valueobject.UserId;
import dri.commerce.user.domain.valueobject.UserPassword;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record UserDomain(
        @Valid
        UserId id,

        @NotBlank(message = "Name cannot be blank")
        String name,

        @Valid
        @NotNull(message = "Email cannot be null")
        UserEmail email,

        @Valid
        @NotNull(message = "Password cannot be null")
        UserPassword password,

        @NotNull(message = "Creation date cannot be null")
        @PastOrPresent(message = "Creation date must be in the past or present")
        LocalDateTime createdAt,

        @PastOrPresent(message = "Update date must be in the past or present")
        LocalDateTime updatedAt,

        @NotNull(message = "Active status cannot be null")
        Boolean active,

        @NotNull(message = "Role cannot be null")
        Role role
) {

    public UserDomain {
        name = name != null ? name.trim() : null;
    }

    public static UserDomain create(String name, UserEmail email, UserPassword password, Role role) {
        LocalDateTime now = LocalDateTime.now();
        return new UserDomain(null, name, email, password, now, now, true, role);
    }

    public static UserDomain restore(UserId id, String name, UserEmail email, UserPassword password, Role role,
                                     LocalDateTime createdAt, LocalDateTime updatedAt, Boolean active) {
        return new UserDomain(id, name, email, password, createdAt, updatedAt, active, role);
    }

    public boolean isActive() {
        return this.active;
    }

    public String getEmailDomain() {
        return this.email.getDomain();
    }

    public UserDomain updateInfo(String newName, UserEmail newEmail) {
        return new UserDomain(
                this.id,
                newName,
                newEmail,
                this.password,
                this.createdAt,
                LocalDateTime.now(),
                this.active,
                this.role
        );
    }

    public UserDomain updatePassword(UserPassword newPassword) {
        return new UserDomain(
                this.id,
                this.name,
                this.email,
                newPassword,
                this.createdAt,
                LocalDateTime.now(),
                this.active,
                this.role
        );
    }

    public UserDomain updateRole(Role newRole) {
        return new UserDomain(
                this.id,
                this.name,
                this.email,
                this.password,
                this.createdAt,
                LocalDateTime.now(),
                this.active,
                newRole
        );
    }

    public UserDomain activate() {
        return new UserDomain(
                this.id,
                this.name,
                this.email,
                this.password,
                this.createdAt,
                LocalDateTime.now(),
                true,
                this.role
        );
    }

    public UserDomain deactivate() {
        return new UserDomain(
                this.id,
                this.name,
                this.email,
                this.password,
                this.createdAt,
                LocalDateTime.now(),
                false,
                this.role
        );
    }

    public UserDomain withId(UserId newId) {
        return new UserDomain(
                newId,
                this.name,
                this.email,
                this.password,
                this.createdAt,
                this.updatedAt,
                this.active,
                this.role
        );
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public boolean isSeller() {
        return this.role == Role.SELLER;
    }

    public boolean isCustomer() {
        return this.role == Role.CUSTOMER;
    }
}