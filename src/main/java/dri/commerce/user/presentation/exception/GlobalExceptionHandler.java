package dri.commerce.user.presentation.exception;

import java.util.List;

import dri.commerce.auth.domain.exception.InvalidCredentialsException;
import dri.commerce.auth.domain.exception.InvalidTokenException;
import dri.commerce.auth.domain.exception.RateLimitExceededException;
import dri.commerce.user.domain.exception.EmailAlreadyExistsException;
import dri.commerce.user.domain.exception.UserNotFoundException;
import dri.commerce.user.domain.exception.WeakPasswordException;
import dri.commerce.user.presentation.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        return switch (exception) {
            case UserNotFoundException ex -> handleNotFound(ex);
            case EmailAlreadyExistsException ex -> handleConflict(ex);
            case WeakPasswordException ex -> handleBadRequest(ex);
            case InvalidCredentialsException ex -> handleUnauthorized(ex);
            case InvalidTokenException ex -> handleForbidden(ex);
            case RateLimitExceededException ex -> handleTooManyRequests(ex);
            case ConstraintViolationException ex -> handleValidation(ex);
            default -> handleGenericError(exception);
        };
    }

    private Response handleNotFound(UserNotFoundException ex) {
        ErrorResponse error = ErrorResponse.of(404, "Not Found", ex.getMessage());
        return Response.status(404).entity(error).build();
    }

    private Response handleConflict(EmailAlreadyExistsException ex) {
        ErrorResponse error = ErrorResponse.of(409, "Conflict", ex.getMessage());
        return Response.status(409).entity(error).build();
    }

    private Response handleBadRequest(WeakPasswordException ex) {
        ErrorResponse error = ErrorResponse.of(400, "Bad Request", ex.getMessage());
        return Response.status(400).entity(error).build();
    }

    private Response handleUnauthorized(InvalidCredentialsException ex) {
        ErrorResponse error = ErrorResponse.of(401, "Unauthorized", ex.getMessage());
        return Response.status(401).entity(error).build();
    }

    private Response handleForbidden(InvalidTokenException ex) {
        ErrorResponse error = ErrorResponse.of(403, "Forbidden", ex.getMessage());
        return Response.status(403).entity(error).build();
    }

    private Response handleTooManyRequests(RateLimitExceededException ex) {
        ErrorResponse error = ErrorResponse.of(
            429, 
            "Too Many Requests", 
            ex.getMessage(),
            List.of(
                String.format("Tentativas restantes: %d", ex.getRemainingAttempts()),
                String.format("Tente novamente em: %d minutos", ex.getMinutesUntilReset())
            )
        );
        return Response.status(429)
                .header("Retry-After", ex.getMinutesUntilReset() * 60) // em segundos
                .entity(error)
                .build();
    }

    private Response handleValidation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        ErrorResponse error = ErrorResponse.of(400, "Validation Error", "Invalid request data", errors);
        return Response.status(400).entity(error).build();
    }

    private Response handleGenericError(Exception ex) {
        ErrorResponse error = ErrorResponse.of(500, "Internal Server Error", "An unexpected error occurred", List.of(ex.getMessage()));
        return Response.status(500).entity(error).build();
    }
}