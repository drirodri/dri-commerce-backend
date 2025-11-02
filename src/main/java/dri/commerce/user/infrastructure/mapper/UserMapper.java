package dri.commerce.user.infrastructure.mapper;

import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.enums.Role;
import dri.commerce.user.domain.valueobject.UserEmail;
import dri.commerce.user.domain.valueobject.UserId;
import dri.commerce.user.domain.valueobject.UserPassword;
import dri.commerce.user.infrastructure.entity.UserEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserMapper {

    public UserEntity toInfrastructure(UserDomain domainUser) {
        if (domainUser == null) {
            return null;
        }

        return new UserEntity(
                domainUser.id() != null ? domainUser.id().value() : null,
                domainUser.name(),
                domainUser.email().value(),
                domainUser.password().value(),
                domainUser.role().getCode(),
                domainUser.createdAt(),
                domainUser.updatedAt(),
                domainUser.active()
        );
    }

    public UserDomain toDomain(UserEntity infraUser) {
        if (infraUser == null) {
            return null;
        }

        UserId id = infraUser.id != null ? UserId.from(infraUser.id) : null;
        UserEmail email = new UserEmail(infraUser.email);
        UserPassword password = new UserPassword(infraUser.password);
        Role role = Role.fromCode(infraUser.role);

        return UserDomain.restore(
                id,
                infraUser.name,
                email,
                password,
                role,
                infraUser.createdAt,
                infraUser.updatedAt,
                infraUser.active
        );
    }
}
