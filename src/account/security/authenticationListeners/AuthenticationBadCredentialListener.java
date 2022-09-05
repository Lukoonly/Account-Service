package account.security.authenticationListeners;

import account.domain.entity.LogEvents;
import account.service.LogService;
import account.service.LoginAttemptService;
import account.util.RequestUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@Configuration
public class AuthenticationBadCredentialListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    LoginAttemptService loginAttemptService;
    LogService logService;
    HttpServletRequest request;


    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {

        logService.saveLog(LogEvents.LOGIN_FAILED, event.getAuthentication().getName(),
                request.getRequestURI().toLowerCase(), request.getRequestURI());
        final String username = event.getAuthentication().getName();
        loginAttemptService.loginFailure(username, request.getRequestURI());
    }
}
