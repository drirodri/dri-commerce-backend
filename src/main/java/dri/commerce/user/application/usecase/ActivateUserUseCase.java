package dri.commerce.user.application.usecase;

import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.exception.UserNotFoundException;
import dri.commerce.user.domain.repository.UserRepository;
import dri.commerce.user.domain.valueobject.UserId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Use case para reativar um usuario desativado
 */
@ApplicationScoped
public class ActivateUserUseCase {

    @Inject
    UserRepository userRepository;

    /**
     * Reativa um usuario previamente desativado
     * 
     * @param id ID do usuario a reativar
     * @return Usuario reativado
     * @throws UserNotFoundException se usuario nao existir
     */
    public UserDomain execute(String id) {
        UserId userId = UserId.from(id);

        UserDomain user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (user.isActive()) {
            return user;
        }

        UserDomain activatedUser = user.activate();

        userRepository.save(activatedUser);

        return activatedUser;
    }
}
