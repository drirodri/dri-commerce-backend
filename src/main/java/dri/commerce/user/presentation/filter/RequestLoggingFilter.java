package dri.commerce.user.presentation.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jboss.logging.Logger;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RequestLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOG = Logger.getLogger(RequestLoggingFilter.class);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final String REQUEST_TIME_KEY = "request-start-time";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty(REQUEST_TIME_KEY, System.currentTimeMillis());

        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();
        String time = LocalDateTime.now().format(TIME_FORMATTER);
        
        LOG.infof("[%s] [REQUEST] %s %s", time, method, path);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Long startTime = (Long) requestContext.getProperty(REQUEST_TIME_KEY);
        long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;

        int status = responseContext.getStatus();
        String statusIcon = getStatusIcon(status);
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();
        String time = LocalDateTime.now().format(TIME_FORMATTER);

        String statusInfo = responseContext.getStatusInfo().getReasonPhrase();
        
        if (status >= 400) {
            Object entity = responseContext.getEntity();
            String errorMessage = entity != null ? entity.toString() : statusInfo;
            LOG.infof("[%s] [RESPONSE] %s %d | %s %s | %dms | %s", time, statusIcon, status, method, path, duration, errorMessage);
        } else {
            LOG.infof("[%s] [RESPONSE] %s %d | %s %s | %dms", time, statusIcon, status, method, path, duration);
        }
    }

    private String getStatusIcon(int status) {
        if (status >= 200 && status < 300) {
            return "[OK]";
        } else if (status >= 400 && status < 500) {
            return "[WARN]";
        } else if (status >= 500) {
            return "[ERROR]";
        } else {
            return "[INFO]";
        }
    }
}
