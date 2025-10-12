package dri.commerce.config;

import com.mongodb.client.MongoClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class MongoHealthCheck implements HealthCheck {

    @Inject
    MongoClient mongoClient;

    @Override
    public HealthCheckResponse call() {
        try {
            mongoClient.getDatabase("dri-commerce").runCommand(new org.bson.Document("ping", 1));
            return HealthCheckResponse.up("MongoDB connection ready");
        } catch (Exception e) {
            return HealthCheckResponse.down("MongoDB connection failed: " + e.getMessage());
        }
    }
}