package dri.commerce.product.presentation.controller;

import java.util.Objects;
import java.util.stream.Collectors;

import dri.commerce.product.application.usecase.ActivateProductUseCase;
import dri.commerce.product.application.usecase.CreateProductUseCase;
import dri.commerce.product.application.usecase.DeactivateProductUseCase;
import dri.commerce.product.application.usecase.FindProductByIdUseCase;
import dri.commerce.product.application.usecase.ListAllProductsUseCase;
import dri.commerce.product.application.usecase.UpdateProductUseCase;
import dri.commerce.product.domain.entity.ProductDomain;
import dri.commerce.product.presentation.dto.request.CreateProductRequest;
import dri.commerce.product.presentation.dto.request.UpdateProductRequest;
import dri.commerce.product.presentation.dto.response.ProductListResponse;
import dri.commerce.product.presentation.dto.response.ProductResponse;
import dri.commerce.user.domain.entity.Page;
import dri.commerce.user.presentation.dto.request.PageRequest;
import dri.commerce.user.presentation.dto.response.MessageResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/api/v1/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    CreateProductUseCase createProductUseCase;

    @Inject
    UpdateProductUseCase updateProductUseCase;

    @Inject
    FindProductByIdUseCase findProductByIdUseCase;

    @Inject
    ListAllProductsUseCase listAllProductsUseCase;

    @Inject
    DeactivateProductUseCase deactivateProductUseCase;

    @Inject
    ActivateProductUseCase activateProductUseCase;

    @POST
    @RolesAllowed({"SELLER", "ADMIN"})
    public Response createProduct(@Valid CreateProductRequest request, @Context SecurityContext securityContext) {
        String sellerId = securityContext.getUserPrincipal().getName();

        CreateProductUseCase.CreateProductCommand command = new CreateProductUseCase.CreateProductCommand(
                request.title(),
                request.price(),
                request.thumbnail(),
                request.availableQuantity(),
                request.condition(),
                request.categoryId(),
                sellerId
        );

        ProductDomain product = createProductUseCase.execute(command);
        ProductResponse response = ProductResponse.fromDomain(product);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"SELLER", "ADMIN"})
    public Response updateProduct(@PathParam("id") String id, @Valid UpdateProductRequest request) {
        UpdateProductUseCase.UpdateProductCommand command = new UpdateProductUseCase.UpdateProductCommand(
                id,
                request.title(),
                request.price(),
                request.thumbnail(),
                request.availableQuantity(),
                request.condition(),
                request.categoryId()
        );

        ProductDomain product = updateProductUseCase.execute(command);
        ProductResponse response = ProductResponse.fromDomain(product);

        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") String id) {
        ProductDomain product = findProductByIdUseCase.execute(id);
        ProductResponse response = ProductResponse.fromDomain(product);

        return Response.ok(response).build();
    }

    @GET
    public Response listProducts(@BeanParam PageRequest pageRequest) {
        int page = pageRequest.page() != null ? pageRequest.page() : 1;
        int pageSize = pageRequest.pageSize() != null ? pageRequest.pageSize() : 20;

        Page<ProductDomain> productPage = listAllProductsUseCase.executePublic(page, pageSize);

        ProductListResponse response = ProductListResponse.fromPage(toResponsePage(productPage));
        return Response.ok(response).build();
    }

    @GET
    @Path("/my")
    @RolesAllowed("SELLER")
    public Response listMyProducts(
            @BeanParam PageRequest pageRequest,
            @Context SecurityContext securityContext
    ) {
        int page = pageRequest.page() != null ? pageRequest.page() : 1;
        int pageSize = pageRequest.pageSize() != null ? pageRequest.pageSize() : 20;
        String sellerId = securityContext.getUserPrincipal().getName();

        Page<ProductDomain> productPage = listAllProductsUseCase.executeBySeller(page, pageSize, sellerId);

        ProductListResponse response = ProductListResponse.fromPage(toResponsePage(productPage));
        return Response.ok(response).build();
    }

    @GET
    @Path("/all")
    @RolesAllowed("ADMIN")
    public Response listAllProducts(@BeanParam PageRequest pageRequest) {
        int page = pageRequest.page() != null ? pageRequest.page() : 1;
        int pageSize = pageRequest.pageSize() != null ? pageRequest.pageSize() : 20;

        Page<ProductDomain> productPage = listAllProductsUseCase.executeAdmin(page, pageSize);

        ProductListResponse response = ProductListResponse.fromPage(toResponsePage(productPage));
        return Response.ok(response).build();
    }

    private Page<ProductResponse> toResponsePage(Page<ProductDomain> productPage) {
        return new Page<>(
            productPage.content().stream()
                .map(ProductResponse::fromDomain)
                .collect(Collectors.toList()),
            productPage.total(),
            productPage.page(),
            productPage.pageSize(),
            productPage.totalPages()
        );
    }

    @PATCH
    @Path("/{id}/deactivate")
    @RolesAllowed({"SELLER", "ADMIN"})
    public Response deactivateProduct(@PathParam("id") String id) {
        ProductDomain product = deactivateProductUseCase.execute(id);
        ProductResponse response = ProductResponse.fromDomain(product);

        return Response.ok(response).build();
    }

    @PATCH
    @Path("/{id}/activate")
    @RolesAllowed({"SELLER", "ADMIN"})
    public Response activateProduct(@PathParam("id") String id) {
        ProductDomain product = activateProductUseCase.execute(id);
        ProductResponse response = ProductResponse.fromDomain(product);

        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"SELLER", "ADMIN"})
    public Response deleteProduct(
            @PathParam("id") String id,
            @Valid dri.commerce.product.presentation.dto.request.DeleteProductRequest request,
            @Context SecurityContext securityContext
    ) {
        String userId = securityContext.getUserPrincipal().getName();
        boolean isAdmin = securityContext.isUserInRole("ADMIN");
        
        DeactivateProductUseCase.HardDeleteCommand command = new DeactivateProductUseCase.HardDeleteCommand(
            id,
            request.confirmationName(),
            userId,
            isAdmin
        );
        
        deactivateProductUseCase.executeHardDelete(command);
        
        return Response.ok(MessageResponse.success("Produto deletado permanentemente")).build();
    }
}
