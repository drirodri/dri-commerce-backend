package dri.commerce.auth.domain.service;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.eclipse.microprofile.jwt.JsonWebToken;

import dri.commerce.auth.domain.exception.InvalidTokenException;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Servico de dominio para validacao de tokens JWT
 */
@ApplicationScoped
public class TokenValidationService {

    @Inject
    JWTParser jwtParser;

    public TokenValidationService() {
        loadPublicKey();
    }
    
    /**
     * Valida e decodifica um token JWT
     * 
     * @param token Token JWT a validar
     * @return JsonWebToken decodificado
     * @throws InvalidTokenException se token for invalido ou expirado
     */
    public JsonWebToken validateAndDecode(String token) {
        try {
            return jwtParser.parse(token);
        } catch (ParseException e) {
            throw new InvalidTokenException("Token invalido ou expirado: " + e.getMessage());
        }
    }

    /**
     * Valida se o token e do tipo refresh
     * 
     * @param jwt Token decodificado
     * @throws InvalidTokenException se nao for refresh token
     */
    public void validateRefreshToken(JsonWebToken jwt) {
        Object type = jwt.getClaim("type");
        if (type == null || !"refresh".equals(type.toString())) {
            throw new InvalidTokenException("Token fornecido nao e um refresh token");
        }
    }

    /**
     * Extrai o user ID do token
     * 
     * @param jwt Token decodificado
     * @return User ID
     */
    public String extractUserId(JsonWebToken jwt) {
        String subject = jwt.getSubject();
        if (subject == null || subject.isBlank()) {
            throw new InvalidTokenException("Token nao contem subject valido");
        }
        return subject;
    }

    /**
     * Carrega a chave publica RSA do arquivo
     * 
     * @return Chave publica RSA
     * @throws RuntimeException se nao conseguir carregar a chave
     */
    private PublicKey loadPublicKey() {
        try (InputStream is = getClass().getResourceAsStream("/META-INF/publicKey.pem")) {
            if (is == null) {
                throw new RuntimeException("Public key file not found at /META-INF/publicKey.pem");
            }

            String key = new String(is.readAllBytes())
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decodedKey = Base64.getDecoder().decode(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key", e);
        }
    }
}
