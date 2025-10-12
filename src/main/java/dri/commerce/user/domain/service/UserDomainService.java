package dri.commerce.user.domain.service;

import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.enums.Role;
import dri.commerce.user.domain.valueobject.UserEmail;
import dri.commerce.user.domain.valueobject.UserPassword;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserDomainService {

    @Inject
    PasswordHashingService passwordHashingService;

    @Inject
    UserValidationService userValidationService;

    public UserDomain createUser(String name, String email, String plainPassword, Role role) {
        UserEmail userEmail = new UserEmail(email);
        userValidationService.validateUserCreation(name, userEmail);

        UserPassword.validateStrength(plainPassword);

        String hashedPassword = passwordHashingService.hash(plainPassword);
        UserPassword userPassword = new UserPassword(hashedPassword);

        return UserDomain.create(name, userEmail, userPassword, role);
    }

    public UserDomain updateUser(UserDomain currentUser, String newName, String newEmail, String newPlainPassword) {
        String finalName = newName != null ? newName : currentUser.name();
        UserEmail finalEmail = newEmail != null ? new UserEmail(newEmail) : currentUser.email();

        userValidationService.validateUserUpdate(currentUser, newName, finalEmail);

        UserDomain updatedUser = currentUser.updateInfo(finalName, finalEmail);

        if (newPlainPassword != null && !newPlainPassword.isBlank()) {
            UserPassword.validateStrength(newPlainPassword);
            String hashedPassword = passwordHashingService.hash(newPlainPassword);
            updatedUser = updatedUser.updatePassword(new UserPassword(hashedPassword));
        }

        return updatedUser;
    }

    /**
     * Verifica se a senha fornecida corresponde à senha do usuário
     * 
     * @param user Usuário
     * @param plainPassword Senha em texto plano
     * @return true se a senha corresponde, false caso contrário
     */
    public boolean verifyPassword(UserDomain user, String plainPassword) {
        return passwordHashingService.verify(plainPassword, user.password().value());
    }

    /**
     * Altera a senha de um usuário validando a senha antiga
     * 
     * @param user Usuário
     * @param oldPassword Senha antiga em texto plano
     * @param newPassword Nova senha em texto plano
     * @return UserDomain com senha atualizada
     */
    public UserDomain changePassword(UserDomain user, String oldPassword, String newPassword) {
        if (!verifyPassword(user, oldPassword)) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException("New password must be different from the old password");
        }

        UserPassword.validateStrength(newPassword);

        String hashedPassword = passwordHashingService.hash(newPassword);
        UserPassword userPassword = new UserPassword(hashedPassword);

        return user.updatePassword(userPassword);
    }

    /**
     * Desativa um usuário (soft delete)
     * 
     * @param user Usuário a ser desativado
     * @return UserDomain desativado
     */
    public UserDomain deactivateUser(UserDomain user) {
        if (!user.isActive()) {
            throw new IllegalStateException("User is already inactive");
        }

        return user.deactivate();
    }

    /**
     * Reativa um usuário
     * 
     * @param user Usuário a ser reativado
     * @return UserDomain reativado
     */
    public UserDomain reactivateUser(UserDomain user) {
        if (user.isActive()) {
            throw new IllegalStateException("User is already active");
        }

        return user.activate();
    }

    /**
     * Verifica se um usuário pode realizar login
     * Regra de negócio: apenas usuários ativos podem fazer login
     * 
     * @param user Usuário
     * @return true se pode fazer login, false caso contrário
     */
    public boolean canLogin(UserDomain user) {
        return user != null && user.isActive();
    }

    /**
     * Verifica se dois usuários têm o mesmo domínio de email
     * Útil para regras de negócio como "usuários da mesma empresa"
     * 
     * @param user1 Primeiro usuário
     * @param user2 Segundo usuário
     * @return true se têm o mesmo domínio
     */
    public boolean haveSameEmailDomain(UserDomain user1, UserDomain user2) {
        return user1.getEmailDomain().equalsIgnoreCase(user2.getEmailDomain());
    }
}