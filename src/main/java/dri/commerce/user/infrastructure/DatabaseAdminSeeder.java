package dri.commerce.user.infrastructure;

import org.jboss.logging.Logger;

import dri.commerce.user.application.usecase.CreateUserUseCase;
import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.enums.Role;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
@IfBuildProfile("dev")
public class DatabaseAdminSeeder {
    @Inject
    CreateUserUseCase createUserUseCase;

    private static final Logger LOG = Logger.getLogger(DatabaseAdminSeeder.class);

    void seedAdmin(@Observes StartupEvent event) {
        LOG.info("DatabaseAdminSeeder: inicializando seed do admin (modo dev)...");
        
        String adminEmail = System.getenv("ADMIN_EMAIL");
        String adminName = System.getenv("ADMIN_NAME");
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        
        if (adminEmail == null || adminPassword == null) {
            LOG.warn("ADMIN_EMAIL ou ADMIN_PASSWORD não definidos. Pulando seed de admin.");
            return;
        }
        
        String name = adminName != null ? adminName : "Admin";
        
        try {
            UserDomain admin = createUserUseCase.execute(name, adminEmail, adminPassword, Role.ADMIN);
            LOG.infof("Usuário ADMIN criado: %s (%s)", admin.name(), admin.email().value());
        } catch (Exception e) {
            LOG.infof("Admin já existe ou erro ao criar: %s", e.getMessage());
        }
    }
}
