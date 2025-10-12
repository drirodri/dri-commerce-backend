package dri.commerce.auth.domain.service;

import dri.commerce.auth.domain.exception.InvalidCredentialsException;
import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.repository.UserRepository;
import dri.commerce.user.domain.service.PasswordHashingService;
import dri.commerce.user.domain.valueobject.UserEmail;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LoginService {

    @Inject
    UserRepository userRepository;

    @Inject
    PasswordHashingService passwordHashingService;

    /**
     * Autentica um usuario com email e senha
     * 
     * @param email Email do usuario
     * @param plainPassword Senha em texto plano
     * @return Usuario autenticado
     * @throws InvalidCredentialsException se credenciais forem invalidas
     */
    public UserDomain authenticate(String email, String plainPassword) {
        UserEmail userEmail = new UserEmail(email);
        UserDomain user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InvalidCredentialsException("Email ou senha invalidos"));

        if (!user.isActive()) {
            throw new InvalidCredentialsException("Usuario inativo");
        }

        boolean passwordMatches = passwordHashingService.verify(plainPassword, user.password().value());
        if (!passwordMatches) {
            throw new InvalidCredentialsException("Email ou senha invalidos");
        }

        return user;
    }

    /**
     * Valida se um usuario pode fazer login
     * 
     * @param user Usuario a validar
     * @return true se pode fazer login
     */
    public boolean canLogin(UserDomain user) {
        return user != null && user.isActive();
    }
}
