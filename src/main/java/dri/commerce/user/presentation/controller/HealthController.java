package dri.commerce.user.presentation.controller;

import com.mongodb.client.MongoClient;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthController {

    @Inject
    MongoClient mongoClient;

    @GET
    public Response healthCheck() {
        try {
            mongoClient.listDatabaseNames().first();
            return Response.ok(new HealthStatus("UP", "Database connected")).build();
        } catch (Exception e) {
            return Response.status(503)
                    .entity(new HealthStatus("DOWN", "Database connection failed"))
                    .build();
        }
    }

    record HealthStatus(String status, String message) {}
}