package dri.commerce.auth.presentation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.ws.rs.NameBinding;

/**
 * Anotacao para aplicar rate limiting em endpoints REST
 * 
 * Exemplo de uso:
 * <pre>
 * {@code
 * @POST
 * @Path("/login")
 * @RateLimit(maxAttempts = 5, windowMinutes = 15)
 * public Response login(LoginRequest request) {
 *     // ...
 * }
 * }
 * </pre>
 * 
 * Se maxAttempts ou windowMinutes forem 0, usa valores do application.properties
 */
@NameBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    
    /**
     * Numero maximo de tentativas permitidas
     * Se 0, usa o valor de rate-limit.login.max-attempts do application.properties
     */
    int maxAttempts() default 0;
    
    /**
     * Janela de tempo em minutos para resetar o contador
     * Se 0, usa o valor de rate-limit.login.window-minutes do application.properties
     */
    int windowMinutes() default 0;
    
    /**
     * Identificador unico para este rate limit (usado para cache)
     * Se vazio, usa o nome do metodo
     */
    String key() default "";
}
