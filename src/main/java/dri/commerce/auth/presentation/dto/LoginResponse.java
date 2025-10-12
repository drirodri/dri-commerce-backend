package dri.commerce.auth.presentation.dto;

public record LoginResponse(
    String token,
    String refreshToken,
    String tokenType,
    Long expiresIn
) {
    
    /**
     * Constructor conveniente que define tokenType como "Bearer"
     */
    public LoginResponse(String token, String refreshToken, Long expiresIn) {
        this(token, refreshToken, "Bearer", expiresIn);
    }
}
