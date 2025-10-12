package dri.commerce.user.presentation.controller;

import org.eclipse.microprofile.jwt.JsonWebToken;

import dri.commerce.user.application.usecase.ActivateUserUseCase;
import dri.commerce.user.application.usecase.CreateUserUseCase;
import dri.commerce.user.application.usecase.DeactivateUserUseCase;
import dri.commerce.user.application.usecase.FindUserByIdUseCase;
import dri.commerce.user.application.usecase.ListAllUsersUseCase;
import dri.commerce.user.application.usecase.UpdateUserUseCase;
import dri.commerce.user.domain.entity.Page;
import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.presentation.dto.request.CreateUserRequest;
import dri.commerce.user.presentation.dto.request.UpdateUserRequest;
import dri.commerce.user.presentation.dto.response.MessageResponse;
import dri.commerce.user.presentation.dto.response.UserListResponse;
import dri.commerce.user.presentation.dto.response.UserResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    CreateUserUseCase createUserUseCase;

    @Inject
    UpdateUserUseCase updateUserUseCase;

    @Inject
    DeactivateUserUseCase deleteUserUseCase;

    @Inject
    FindUserByIdUseCase findUserByIdUseCase;

    @Inject
    ListAllUsersUseCase listAllUsersUseCase;

    @Inject
    ActivateUserUseCase activateUserUseCase;

    @Inject
    JsonWebToken jwt;

    @Context
    SecurityContext securityContext;

    /**
     * Cria um novo usuario (registro publico)
     * POST /api/v1/users
     * 
     * Acesso: Publico (@PermitAll)
     * Qualquer pessoa pode se registrar como CUSTOMER
     */
    @POST
    @PermitAll
    public Response createUser(@Valid CreateUserRequest request) {
        UserDomain user = createUserUseCase.execute(
                request.name(),
                request.email(),
                request.password(),
                request.role()
        );

        UserResponse response = UserResponse.fromDomain(user);

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    /**
     * Busca usuario por ID
     * GET /api/v1/users/{id}
     * 
     * Acesso:
     * - ADMIN: pode ver qualquer usuario
     * - CUSTOMER/SELLER: pode ver apenas seus proprios dados
     */
    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "CUSTOMER", "SELLER"})
    public Response getUserById(@PathParam("id") String id) {
        String currentUserId = jwt.getSubject();
        boolean isAdmin = securityContext.isUserInRole("ADMIN");
        
        if (!isAdmin && !currentUserId.equals(id)) {
            throw new ForbiddenException("Voce nao tem permissao para acessar dados de outro usuario");
        }

        UserDomain user = findUserByIdUseCase.execute(id);

        UserResponse response = UserResponse.fromDomain(user);

        return Response.ok(response).build();
    }

    /**
     * Lista todos os usuarios com paginacao
     * GET /api/v1/users?page=1&pageSize=10
     * 
     * Acesso: Apenas ADMIN
     */
    @GET
    @RolesAllowed({"ADMIN"})
    public Response listUsers(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("pageSize") @DefaultValue("10") int pageSize
    ) {
        Page<UserDomain> userPage = listAllUsersUseCase.execute(page, pageSize);

        Page<UserResponse> responsePage = Page.of(
                userPage.content().stream()
                        .map(UserResponse::fromDomain)
                        .toList(),
                userPage.total(),
                userPage.page(),
                userPage.pageSize()
        );

        UserListResponse response = UserListResponse.fromPage(responsePage);

        return Response.ok(response).build();
    }

    /**
     * Atualiza dados do usuario
     * PUT /api/v1/users/{id}
     * 
     * Acesso:
     * - ADMIN: pode atualizar qualquer usuario
     * - CUSTOMER/SELLER: pode atualizar apenas seus proprios dados
     */
    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "CUSTOMER", "SELLER"})
    public Response updateUser(
            @PathParam("id") String id,
            @Valid UpdateUserRequest request
    ) {
        String currentUserId = jwt.getSubject();
        boolean isAdmin = securityContext.isUserInRole("ADMIN");
        
        if (!isAdmin && !currentUserId.equals(id)) {
            throw new ForbiddenException("Voce nao tem permissao para atualizar dados de outro usuario");
        }

        UserDomain user = updateUserUseCase.execute(
                id,
                request.name(),
                request.email(),
                request.password()
        );

        UserResponse response = UserResponse.fromDomain(user);

        return Response.ok(response).build();
    }

    /**
     * Desativa um usuario (soft delete)
     * DELETE /api/v1/users/{id}
     * 
     * Acesso: Apenas ADMIN
     * Nota: Nao deleta fisicamente, apenas marca como inativo
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    public Response deleteUser(@PathParam("id") String id) {
        deleteUserUseCase.execute(id);

        MessageResponse response = MessageResponse.success("User deactivated successfully");

        return Response.ok(response).build();
    }

    /**
     * Reativa um usuario previamente desativado
     * POST /api/v1/users/{id}/activate
     * 
     * Acesso: Apenas ADMIN
     */
    @POST
    @Path("/{id}/activate")
    @RolesAllowed({"ADMIN"})
    public Response activateUser(@PathParam("id") String id) {
        UserDomain user = activateUserUseCase.execute(id);

        UserResponse response = UserResponse.fromDomain(user);

        return Response.ok(response).build();
    }
}