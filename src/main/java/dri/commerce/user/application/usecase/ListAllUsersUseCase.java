package dri.commerce.user.application.usecase;

import dri.commerce.user.domain.entity.Page;
import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ListAllUsersUseCase {

    @Inject
    UserRepository userRepository;

    public Page<UserDomain> execute(int page, int pageSize) {
        return userRepository.findAll(page, pageSize);
    }
}