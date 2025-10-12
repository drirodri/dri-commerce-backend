package dri.commerce.auth.presentation.controller;

import org.eclipse.microprofile.jwt.JsonWebToken;

import dri.commerce.auth.application.usecase.LoginUseCase;
import dri.commerce.auth.application.usecase.RefreshTokenUseCase;
import dri.commerce.auth.presentation.annotation.RateLimit;
import dri.commerce.auth.presentation.dto.LoginRequest;
import dri.commerce.auth.presentation.dto.LoginResponse;
import dri.commerce.auth.presentation.dto.MeResponse;
import dri.commerce.auth.presentation.dto.RefreshTokenRequest;
import dri.commerce.auth.presentation.dto.RefreshTokenResponse;
import dri.commerce.user.application.usecase.FindUserByIdUseCase;
import dri.commerce.user.domain.entity.UserDomain;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    LoginUseCase loginUseCase;

    @Inject
    RefreshTokenUseCase refreshTokenUseCase;

    @Inject
    FindUserByIdUseCase findUserByIdUseCase;

    @Inject
    JsonWebToken jwt;  


    /**
     * Endpoint de login com rate limiting
     * POST /api/v1/auth/login
     * 
     * Rate limit: 5 tentativas a cada 15 minutos por IP
     * 
     * @param request contém email e senha
     * @return 200 OK com token, refreshToken e expiresIn
     * @throws InvalidCredentialsException se credenciais estiverem incorretas (401)
     * @throws RateLimitExceededException se limite de tentativas for excedido (429)
     */
    @POST
    @Path("/login")
    @PermitAll
    @RateLimit(maxAttempts = 5, windowMinutes = 15, key = "login")
    public Response login(@Valid LoginRequest request) {
        var result = loginUseCase.execute(request.email(), request.password());
        
        var response = new LoginResponse(
            result.token(),
            result.refreshToken(),
            result.expiresIn()
        );
        
        return Response.ok(response).build();
    }

    /**
     * Endpoint de renovacao de token
     * POST /api/v1/auth/refresh
     * 
     * @param request contém refreshToken
     * @return 200 OK com novo accessToken e expiresIn
     * @throws InvalidTokenException se refresh token for invalido ou expirado
     */
    @POST
    @Path("/refresh")
    @PermitAll
    public Response refresh(@Valid RefreshTokenRequest request) {
        var result = refreshTokenUseCase.execute(request.refreshToken());
        
        var response = new RefreshTokenResponse(
            result.token(),
            result.expiresIn()
        );
        
        return Response.ok(response).build();
    }

    @GET
    @Path("/me")
    @RolesAllowed({"ADMIN", "CUSTOMER", "SELLER"})
    public Response getCurrentUser() {
        String userId = jwt.getSubject();
        UserDomain user = findUserByIdUseCase.execute(userId);

        MeResponse userResponse = new MeResponse(
            user.id().value(),
            user.name(),
            user.email().value(),
            user.role()
        );

        return Response.ok(userResponse).build();
    }
}
