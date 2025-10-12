package dri.commerce.auth.presentation.filter;

import java.io.IOException;
import java.lang.reflect.Method;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import dri.commerce.auth.domain.exception.RateLimitExceededException;
import dri.commerce.auth.domain.service.RateLimitService;
import dri.commerce.auth.presentation.annotation.RateLimit;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;

/**
 * Filter que intercepta requisicoes com anotacao @RateLimit
 * e aplica rate limiting baseado no IP do cliente
 */
@Provider
@RateLimit
@Priority(Priorities.USER)
public class RateLimitFilter implements ContainerRequestFilter {

    @Inject
    RateLimitService rateLimitService;

    @Context
    ResourceInfo resourceInfo;

    @ConfigProperty(name = "rate-limit.login.max-attempts", defaultValue = "5")
    int defaultMaxAttempts;

    @ConfigProperty(name = "rate-limit.login.window-minutes", defaultValue = "15")
    int defaultWindowMinutes;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        
        if (method == null) {
            return;
        }

        RateLimit rateLimitAnnotation = method.getAnnotation(RateLimit.class);
        
        if (rateLimitAnnotation == null) {
            // Verifica se a anotacao esta na classe
            rateLimitAnnotation = resourceInfo.getResourceClass().getAnnotation(RateLimit.class);
        }

        if (rateLimitAnnotation == null) {
            return;
        }

        // Extrai configuracoes da anotacao ou usa defaults
        int maxAttempts = rateLimitAnnotation.maxAttempts() > 0 
            ? rateLimitAnnotation.maxAttempts() 
            : defaultMaxAttempts;
            
        int windowMinutes = rateLimitAnnotation.windowMinutes() > 0 
            ? rateLimitAnnotation.windowMinutes() 
            : defaultWindowMinutes;

        // Extrai IP do cliente
        String ipAddress = extractIpAddress(requestContext);
        
        // Cria uma chave unica baseada no endpoint e IP
        String rateLimitKey = rateLimitAnnotation.key().isEmpty()
            ? method.getName() + ":" + ipAddress
            : rateLimitAnnotation.key() + ":" + ipAddress;

        // Verifica rate limit
        if (!rateLimitService.allowRequest(rateLimitKey, maxAttempts, windowMinutes)) {
            int remaining = rateLimitService.getRemainingAttempts(rateLimitKey, maxAttempts, windowMinutes);
            long minutesUntilReset = rateLimitService.getMinutesUntilReset(rateLimitKey, windowMinutes);
            throw new RateLimitExceededException(remaining, minutesUntilReset);
        }
    }

    /**
     * Extrai o endereco IP real do cliente considerando proxies
     * Verifica headers X-Forwarded-For e X-Real-IP
     */
    private String extractIpAddress(ContainerRequestContext requestContext) {
        // Verifica X-Forwarded-For (usado por proxies e load balancers)
        String xForwardedFor = requestContext.getHeaderString("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        // Verifica X-Real-IP (usado por alguns proxies como Nginx)
        String xRealIp = requestContext.getHeaderString("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp.trim();
        }
        
        // Fallback
        return "unknown";
    }
}
