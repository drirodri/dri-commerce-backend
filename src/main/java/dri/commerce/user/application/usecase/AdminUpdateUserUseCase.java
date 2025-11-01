package dri.commerce.user.application.usecase;

import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.enums.Role;
import dri.commerce.user.domain.exception.UserNotFoundException;
import dri.commerce.user.domain.repository.UserRepository;
import dri.commerce.user.domain.service.UserDomainService;
import dri.commerce.user.domain.valueobject.UserId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AdminUpdateUserUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    UserDomainService userDomainService;

    public UserDomain execute(String id, String name, String email, Role role, Boolean active) {
        UserId userId = UserId.from(id);

        UserDomain currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        UserDomain updatedUser = userDomainService.adminUpdateUser(currentUser, name, email, role, active);

        return userRepository.update(updatedUser);
    }
}
