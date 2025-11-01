package dri.commerce.user.application.usecase;

import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.exception.UserNotFoundException;
import dri.commerce.user.domain.repository.UserRepository;
import dri.commerce.user.domain.service.UserValidationService;
import dri.commerce.user.domain.valueobject.UserId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Use case para desativar (soft delete) um usuario
 * Nao deleta fisicamente, apenas marca como inativo (active = false)
 */
@ApplicationScoped
public class DeactivateUserUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    UserValidationService userValidationService;

    /**
     * Desativa um usuario (soft delete)
     * 
     * @param id ID do usuario a desativar
     * @throws UserNotFoundException se usuario nao existir
     */
    public void execute(String id) {
        UserId userId = UserId.from(id);

        UserDomain user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userValidationService.validateUserDeletion(user);

        UserDomain deactivatedUser = user.deactivate();

        userRepository.update(deactivatedUser);
    }
}
