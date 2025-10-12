package dri.commerce.auth.application.usecase;

import dri.commerce.auth.domain.service.JwtTokenService;
import dri.commerce.auth.domain.service.LoginService;
import dri.commerce.user.domain.entity.UserDomain;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LoginUseCase {

    @Inject
    LoginService loginService;

    @Inject
    JwtTokenService jwtTokenService;

    /**
     * Record que representa o resultado do login
     * 
     * @param token Token JWT de acesso
     * @param refreshToken Token de refresh
     * @param expiresIn Tempo de expiracao em segundos
     */
    public record LoginResult(String token, String refreshToken, Long expiresIn) {}

    /**
     * Executa o caso de uso de login
     * 
     * @param email Email do usuario
     * @param password Senha em texto plano
     * @return LoginResult com tokens e tempo de expiracao
     */
    public LoginResult execute(String email, String password) {
        // 1. Autenticar usuario
        UserDomain user = loginService.authenticate(email, password);

        // 2. Gerar tokens JWT
        String token = jwtTokenService.generateToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);

        // 3. Retornar resultado (expiresIn em segundos - 1 hora = 3600s)
        return new LoginResult(token, refreshToken, 3600L);
    }
}
