package dri.commerce.auth.application.usecase;

import org.eclipse.microprofile.jwt.JsonWebToken;

import dri.commerce.auth.domain.exception.InvalidTokenException;
import dri.commerce.auth.domain.service.JwtTokenService;
import dri.commerce.auth.domain.service.TokenValidationService;
import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.repository.UserRepository;
import dri.commerce.user.domain.valueobject.UserId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Use case para renovacao de access token usando refresh token
 */
@ApplicationScoped
public class RefreshTokenUseCase {

    @Inject
    TokenValidationService tokenValidationService;

    @Inject
    UserRepository userRepository;

    @Inject
    JwtTokenService jwtTokenService;

    /**
     * Resultado da renovacao de token
     * 
     * @param token Novo access token
     * @param expiresIn Tempo de expiracao em segundos
     */
    public record RefreshResult(String token, Long expiresIn) {
    }

    /**
     * Renova o access token usando um refresh token valido
     * 
     * @param refreshToken Refresh token valido
     * @return RefreshResult com novo access token
     * @throws InvalidTokenException se refresh token for invalido
     */
    public RefreshResult execute(String refreshToken) {
        JsonWebToken jwt = tokenValidationService.validateAndDecode(refreshToken);

        tokenValidationService.validateRefreshToken(jwt);

        String userIdStr = tokenValidationService.extractUserId(jwt);
        UserId userId = new UserId(userIdStr);

        UserDomain user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidTokenException("Usuario nao encontrado"));

        if (!user.isActive()) {
            throw new InvalidTokenException("Usuario inativo");
        }

        String newAccessToken = jwtTokenService.generateToken(user);

        return new RefreshResult(newAccessToken, 3600L);
    }
}
