package dri.commerce.user.infrastructure;

import dri.commerce.user.application.usecase.CreateUserUseCase;
import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.enums.Role;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class DatabaseAdminSeeder {
    @Inject
    CreateUserUseCase createUserUseCase;

    private static final Logger LOG = Logger.getLogger(DatabaseAdminSeeder.class);

    void seedAdmin(@Observes StartupEvent event) {
        LOG.info("DatabaseAdminSeeder: inicializando seed do admin...");
        String adminEmail = "admin@dricommerce.com";
        String adminName = "Admin Principal";
        String adminPassword = "Admin@Dri123";
        try {
            UserDomain admin = createUserUseCase.execute(adminName, adminEmail, adminPassword, Role.ADMIN);
            LOG.infof("Usuário ADMIN criado: %s (%s)", admin.name(), admin.email().value());
        } catch (Exception e) {
            LOG.infof("Admin já existe ou erro ao criar: %s", e.getMessage());
        }
    }
}
