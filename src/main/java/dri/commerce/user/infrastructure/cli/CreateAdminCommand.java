package dri.commerce.user.infrastructure.cli;

import dri.commerce.user.application.usecase.CreateUserUseCase;
import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.enums.Role;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "create-admin", description = "Cria um usuário ADMIN no sistema", mixinStandardHelpOptions = true)
@Dependent
public class CreateAdminCommand implements Runnable {

    @Option(names = {"--name"}, description = "Nome do admin", required = true)
    String name;

    @Option(names = {"--email"}, description = "Email do admin", required = true)
    String email;

    @Option(names = {"--password"}, description = "Senha do admin", required = true)
    String password;

    @Inject
    CreateUserUseCase createUserUseCase;

    @Override
    public void run() {
        try {
            UserDomain admin = createUserUseCase.execute(name, email, password, Role.ADMIN);
            System.out.printf("Usuário ADMIN criado: %s (%s)%n", admin.name(), admin.email());
        } catch (Exception e) {
            System.err.println("Erro ao criar admin: " + e.getMessage());
        }
    }
}
