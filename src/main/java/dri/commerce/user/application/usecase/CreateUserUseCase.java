package dri.commerce.user.application.usecase;

import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.enums.Role;
import dri.commerce.user.domain.repository.UserRepository;
import dri.commerce.user.domain.service.UserDomainService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateUserUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    UserDomainService userDomainService;

    public UserDomain execute(String name, String email, String password, Role role) {
        
        UserDomain newUser = userDomainService.createUser(name, email, password, role);
        
        return userRepository.save(newUser);
    }
}