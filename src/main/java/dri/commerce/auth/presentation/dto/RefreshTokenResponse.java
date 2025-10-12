package dri.commerce.auth.presentation.dto;

public record RefreshTokenResponse(
    String token,
    String tokenType,
    Long expiresIn
) {
    
    /**
     * Constructor conveniente que define tokenType como "Bearer"
     */
    public RefreshTokenResponse(String token, Long expiresIn) {
        this(token, "Bearer", expiresIn);
    }
}
