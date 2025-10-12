package dri.commerce.user.application.usecase;

import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.exception.UserNotFoundException;
import dri.commerce.user.domain.repository.UserRepository;
import dri.commerce.user.domain.valueobject.UserId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FindUserByIdUseCase {

    @Inject
    UserRepository userRepository;

    public UserDomain execute(String id) {
        UserId userId = UserId.from(id);

        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    } 
}
