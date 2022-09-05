package account.security.authenticationListeners;

import account.service.LogService;
import account.service.LoginAttemptService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@Component
public class AuthenticationEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        loginAttemptService.loginSuccess(event.getAuthentication().getName());
    }
}
