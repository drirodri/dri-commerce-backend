package dri.commerce.user.domain.service;

import io.quarkus.elytron.security.common.BcryptUtil;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PasswordHashingService {
    private static final int BCRYPT_ROUNDS = 12;

    public String hash(String plainPassword) {
        return BcryptUtil.bcryptHash(plainPassword, BCRYPT_ROUNDS);
    }

    public boolean verify(String plainPassword, String hashedPassword) {
        return BcryptUtil.matches(plainPassword, hashedPassword);
    }
}
