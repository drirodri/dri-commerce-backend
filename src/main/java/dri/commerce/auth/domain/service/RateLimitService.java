package dri.commerce.auth.domain.service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Servico para controle de rate limiting de tentativas de login
 * Usa cache em memoria para rastrear tentativas por IP
 */
@ApplicationScoped
public class RateLimitService {

    @ConfigProperty(name = "rate-limit.login.max-attempts", defaultValue = "5")
    Integer maxAttempts;

    @ConfigProperty(name = "rate-limit.login.window-minutes", defaultValue = "15")
    Integer windowMinutes;

    private final ConcurrentMap<String, RateLimitAttempt> attempts = new ConcurrentHashMap<>();

    /**
     * Verifica se uma requisicao pode prosseguir baseado no rate limit
     * 
     * @param key Chave unica identificando o recurso/cliente (ex: "login:192.168.1.1")
     * @param maxAttempts Numero maximo de tentativas permitidas
     * @param windowMinutes Janela de tempo em minutos
     * @return true se ainda esta dentro do limite, false se excedeu
     */
    public boolean allowRequest(String key, int maxAttempts, int windowMinutes) {
        cleanExpiredAttempts(windowMinutes);
        
        RateLimitAttempt attempt = attempts.compute(key, (k, existing) -> {
            if (existing == null || existing.isExpired(windowMinutes)) {
                return new RateLimitAttempt(1, Instant.now());
            }
            return existing.increment();
        });

        return attempt.count() <= maxAttempts;
    }

    /**
     * Registra um evento bem-sucedido, limpando o contador
     * 
     * @param key Chave unica identificando o recurso/cliente
     */
    public void recordSuccess(String key) {
        attempts.remove(key);
    }

    /**
     * Retorna quantas tentativas restam para uma chave
     * 
     * @param key Chave unica identificando o recurso/cliente
     * @param maxAttempts Numero maximo de tentativas permitidas
     * @param windowMinutes Janela de tempo em minutos
     * @return Numero de tentativas restantes
     */
    public int getRemainingAttempts(String key, int maxAttempts, int windowMinutes) {
        RateLimitAttempt attempt = attempts.get(key);
        if (attempt == null || attempt.isExpired(windowMinutes)) {
            return maxAttempts;
        }
        return Math.max(0, maxAttempts - attempt.count());
    }

    /**
     * Retorna tempo ate resetar o limite para uma chave (em minutos)
     * 
     * @param key Chave unica identificando o recurso/cliente
     * @param windowMinutes Janela de tempo em minutos
     * @return Minutos ate o reset, ou 0 se ja expirou
     */
    public long getMinutesUntilReset(String key, int windowMinutes) {
        RateLimitAttempt attempt = attempts.get(key);
        if (attempt == null || attempt.isExpired(windowMinutes)) {
            return 0;
        }
        
        Instant resetTime = attempt.firstAttemptTime().plus(Duration.ofMinutes(windowMinutes));
        Duration timeUntilReset = Duration.between(Instant.now(), resetTime);
        
        return Math.max(0, timeUntilReset.toMinutes() + 1);
    }

    /**
     * Remove tentativas expiradas do cache (cleanup periodico)
     */
    private void cleanExpiredAttempts(int windowMinutes) {
        attempts.entrySet().removeIf(entry -> 
            entry.getValue().isExpired(windowMinutes)
        );
    }

    // ========== Métodos legados para compatibilidade ==========
    
    /**
     * @deprecated Use allowRequest(String, int, int) para mais flexibilidade
     */
    @Deprecated
    public boolean allowLogin(String ipAddress) {
        return allowRequest(ipAddress, maxAttempts, windowMinutes);
    }

    /**
     * @deprecated Use recordSuccess(String) para mais flexibilidade
     */
    @Deprecated
    public void recordSuccessfulLogin(String ipAddress) {
        recordSuccess(ipAddress);
    }

    /**
     * @deprecated Use getRemainingAttempts(String, int, int) para mais flexibilidade
     */
    @Deprecated
    public int getRemainingAttempts(String ipAddress) {
        return getRemainingAttempts(ipAddress, maxAttempts, windowMinutes);
    }

    /**
     * @deprecated Use getMinutesUntilReset(String, int) para mais flexibilidade
     */
    @Deprecated
    public long getMinutesUntilReset(String ipAddress) {
        return getMinutesUntilReset(ipAddress, windowMinutes);
    }

    /**
     * Record que armazena informacoes sobre tentativas de rate limit
     * 
     * @param count Numero de tentativas
     * @param firstAttemptTime Timestamp da primeira tentativa
     */
    private record RateLimitAttempt(int count, Instant firstAttemptTime) {
        
        public RateLimitAttempt increment() {
            return new RateLimitAttempt(count + 1, firstAttemptTime);
        }

        public boolean isExpired(int windowMinutes) {
            Instant expiryTime = firstAttemptTime.plus(Duration.ofMinutes(windowMinutes));
            return Instant.now().isAfter(expiryTime);
        }
    }
}
