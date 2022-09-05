package account.security.handler;

import account.domain.entity.LogEvents;
import account.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    LogService logService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getOutputStream().println(new ObjectMapper().writeValueAsString(Map.of(
                "timestamp", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()),
                "status", 403,
                "error", "Forbidden",
                "message", "Access Denied!",
                "path", request.getRequestURI()
        )));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog(LogEvents.ACCESS_DENIED, email, request.getRequestURI().toLowerCase(), request.getRequestURI());
    }
}
