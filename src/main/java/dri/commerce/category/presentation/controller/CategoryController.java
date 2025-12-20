package dri.commerce.category.presentation.controller;

import java.util.List;
import java.util.stream.Collectors;

import dri.commerce.category.application.usecase.CreateCategoryUseCase;
import dri.commerce.category.application.usecase.DeleteCategoryUseCase;
import dri.commerce.category.application.usecase.FindCategoryByIdUseCase;
import dri.commerce.category.application.usecase.ListCategoriesUseCase;
import dri.commerce.category.application.usecase.UpdateCategoryUseCase;
import dri.commerce.category.domain.entity.CategoryDomain;
import dri.commerce.category.presentation.dto.request.CreateCategoryRequest;
import dri.commerce.category.presentation.dto.request.UpdateCategoryRequest;
import dri.commerce.category.presentation.dto.response.CategoryListResponse;
import dri.commerce.category.presentation.dto.response.CategoryResponse;
import dri.commerce.user.presentation.dto.response.MessageResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryController {

    @Inject
    CreateCategoryUseCase createCategoryUseCase;

    @Inject
    UpdateCategoryUseCase updateCategoryUseCase;

    @Inject
    DeleteCategoryUseCase deleteCategoryUseCase;

    @Inject
    ListCategoriesUseCase listCategoriesUseCase;

    @Inject
    FindCategoryByIdUseCase findCategoryByIdUseCase;

    @GET
    @PermitAll
    public Response listCategories() {
        List<CategoryDomain> categories = listCategoriesUseCase.execute();

        List<CategoryResponse> responseList = categories.stream()
                .map(CategoryResponse::fromDomain)
                .collect(Collectors.toList());

        CategoryListResponse response = CategoryListResponse.fromList(responseList);
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    @PermitAll
    public Response getCategoryById(@PathParam("id") Long id) {
        CategoryDomain category = findCategoryByIdUseCase.execute(id);
        CategoryResponse response = CategoryResponse.fromDomain(category);

        return Response.ok(response).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response createCategory(@Valid CreateCategoryRequest request) {
        CreateCategoryUseCase.CreateCategoryCommand command = new CreateCategoryUseCase.CreateCategoryCommand(
                request.name(),
                request.description()
        );

        CategoryDomain category = createCategoryUseCase.execute(command);
        CategoryResponse response = CategoryResponse.fromDomain(category);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response updateCategory(@PathParam("id") Long id, @Valid UpdateCategoryRequest request) {
        UpdateCategoryUseCase.UpdateCategoryCommand command = new UpdateCategoryUseCase.UpdateCategoryCommand(
                id,
                request.name(),
                request.description()
        );

        CategoryDomain category = updateCategoryUseCase.execute(command);
        CategoryResponse response = CategoryResponse.fromDomain(category);

        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response deleteCategory(@PathParam("id") Long id) {
        deleteCategoryUseCase.execute(id);

        return Response.ok(MessageResponse.success("Categoria deletada com sucesso")).build();
    }
}
