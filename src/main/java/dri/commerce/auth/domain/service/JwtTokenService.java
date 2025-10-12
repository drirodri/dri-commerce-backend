package dri.commerce.auth.domain.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import dri.commerce.user.domain.entity.UserDomain;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtTokenService {

    @ConfigProperty(name = "jwt.token.ttl", defaultValue = "3600")
    Long tokenTtl;

    @ConfigProperty(name = "jwt.refresh.ttl", defaultValue = "604800")
    Long refreshTokenTtl;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    private final PrivateKey privateKey;

    public JwtTokenService() {
        this.privateKey = loadPrivateKey();
    }

    /**
     * Gera um token JWT de acesso para o usuario
     * 
     * @param user Usuario para gerar o token
     * @return Token JWT assinado
     */
    public String generateToken(UserDomain user) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(tokenTtl);

        return Jwt.issuer(issuer)
                .subject(user.id().value())
                .claim("name", user.name())
                .claim("email", user.email().value())
                .groups(user.role().name())
                .issuedAt(now)
                .expiresAt(exp)
                .sign(privateKey);
    }

    /**
     * Gera um refresh token de longa duracao
     * 
     * @param user Usuario para gerar o refresh token
     * @return Refresh token JWT assinado
     */
    public String generateRefreshToken(UserDomain user) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(refreshTokenTtl);

        return Jwt.issuer(issuer)
                .subject(user.id().value())
                .claim("type", "refresh")
                .issuedAt(now)
                .expiresAt(exp)
                .sign(privateKey);
    }

    /**
     * Carrega a chave privada RSA do arquivo
     * 
     * @return Chave privada RSA
     * @throws RuntimeException se nao conseguir carregar a chave
     */
    private PrivateKey loadPrivateKey() {
        try (InputStream is = getClass().getResourceAsStream("/META-INF/privateKey.pem")) {
            if (is == null) {
                throw new RuntimeException("Private key file not found at /META-INF/privateKey.pem");
            }

            String key = new String(is.readAllBytes())
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decodedKey = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Failed to load private key", e);
        }
    }
}
