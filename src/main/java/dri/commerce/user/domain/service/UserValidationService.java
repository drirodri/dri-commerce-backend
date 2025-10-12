package dri.commerce.user.domain.service;

import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.exception.EmailAlreadyExistsException;
import dri.commerce.user.domain.repository.UserRepository;
import dri.commerce.user.domain.valueobject.UserEmail;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Domain Service responsável por validações de negócio relacionadas a usuários.
 * Centraliza regras que não pertencem a uma única entidade.
 */
@ApplicationScoped
public class UserValidationService {

    @Inject
    UserRepository userRepository;

    /**
     * Valida se o email já está em uso por outro usuário
     * 
     * @param email Email a ser validado
     * @throws EmailAlreadyExistsException se o email já existir
     */
    public void validateEmailUniqueness(UserEmail email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists: " + email.value());
        }
    }

    /**
     * Valida se o email pode ser alterado (não está em uso por outro usuário)
     * 
     * @param newEmail Novo email desejado
     * @param currentUser Usuário atual que deseja alterar o email
     * @throws EmailAlreadyExistsException se o email já estiver em uso por outro usuário
     */
    public void validateEmailChangeAllowed(UserEmail newEmail, UserDomain currentUser) {
        // Se o email não mudou, não precisa validar
        if (currentUser.email().equals(newEmail)) {
            return;
        }

        // Verifica se o novo email já está em uso
        userRepository.findByEmail(newEmail).ifPresent(existingUser -> {
            // Se o usuário encontrado não é o atual, lança exceção
            if (!existingUser.id().equals(currentUser.id())) {
                throw new EmailAlreadyExistsException("Email already in use: " + newEmail.value());
            }
        });
    }

    /**
     * Valida regras de negócio para criação de usuário
     * 
     * @param name Nome do usuário
     * @param email Email do usuário
     */
    public void validateUserCreation(String name, UserEmail email) {
        validateNameRequirements(name);
        validateEmailUniqueness(email);
        validateEmailDomainAllowed(email);
    }

    /**
     * Valida regras de negócio para atualização de usuário
     * 
     * @param currentUser Usuário atual
     * @param newName Novo nome (pode ser null)
     * @param newEmail Novo email (pode ser null)
     */
    public void validateUserUpdate(UserDomain currentUser, String newName, UserEmail newEmail) {
        if (newName != null) {
            validateNameRequirements(newName);
        }

        if (newEmail != null) {
            validateEmailChangeAllowed(newEmail, currentUser);
            validateEmailDomainAllowed(newEmail);
        }
    }

    /**
     * Valida requisitos do nome do usuário
     */
    private void validateNameRequirements(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (name.trim().length() < 3) {
            throw new IllegalArgumentException("Name must have at least 3 characters");
        }

        if (name.length() > 100) {
            throw new IllegalArgumentException("Name cannot exceed 100 characters");
        }

        // Valida que o nome contém apenas letras e espaços
        if (!name.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            throw new IllegalArgumentException("Name can only contain letters and spaces");
        }
    }

    /**
     * Valida se o domínio do email é permitido
     * Exemplo de regra de negócio: bloquear emails temporários
     */
    private void validateEmailDomainAllowed(UserEmail email) {
        String domain = email.getDomain();
        
        // Lista de domínios bloqueados (emails temporários)
        if (isTemporaryEmailDomain(domain)) {
            throw new IllegalArgumentException("Temporary email addresses are not allowed: " + domain);
        }
    }

    /**
     * Verifica se é um domínio de email temporário
     */
    private boolean isTemporaryEmailDomain(String domain) {
        // Adicione aqui domínios de emails temporários que deseja bloquear
        return domain.matches(".*(tempmail|guerrillamail|10minutemail|throwaway).*");
    }

    /**
     * Valida se um usuário pode ser deletado
     * 
     * @param user Usuário a ser deletado
     */
    public void validateUserDeletion(UserDomain user) {
        // Exemplo: não permitir deletar o último admin (se houver essa lógica)
        // Ou outras regras de negócio relacionadas à deleção
        
        if (!user.isActive()) {
            throw new IllegalStateException("Cannot delete an inactive user. User is already deactivated.");
        }
    }
}