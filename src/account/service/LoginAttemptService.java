package account.service;

import account.domain.entity.Account;
import account.domain.entity.LogEvents;
import account.domain.exceptions.AccountNotFoundException;
import account.domain.repository.AccountRep;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class LoginAttemptService {

    private final static int MAX_ATTEMPT = 6;
    @Autowired
    private AccountRep accountRep;
    @Autowired
    private LogService logService;


    public void loginFailure(String email, String uri) {

        Optional<Account> accountOptional = accountRep.findUserByEmailIgnoreCase(email);
        if (accountOptional.isEmpty()) {
            return;
        }
        Account account = accountOptional.get();
        if (account.getRoles().contains("ROLE_ADMINISTRATOR")) {
            return;
        }

        account.setFailedAttempt(account.getFailedAttempt() + 1);

        if (account.getFailedAttempt() == MAX_ATTEMPT) {
            logService.saveLog(LogEvents.BRUTE_FORCE, email, uri.toLowerCase(), uri);
            account.setAccountNonLocked(false);
            logService.saveLog(LogEvents.LOCK_USER, email, String.format("Lock user %s", email), uri);
        }
        accountRep.save(account);
    }

    public void loginSuccess(String email) {
        Account account = accountRep.findUserByEmailIgnoreCase(email).orElseThrow(AccountNotFoundException::new);
        accountRep.save(account);
    }
}
