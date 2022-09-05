package account.service;

import account.api.dto.in.AccountSignUpDTO;
import account.api.dto.in.NewPasswordDTO;
import account.api.mapper.AccountMapper;
import account.domain.entity.Account;
import account.domain.entity.LogEvents;
import account.domain.exceptions.AccountNotFoundException;
import account.domain.exceptions.BadRequestException;
import account.domain.repository.AccountRep;
import account.domain.repository.LogRep;
import account.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class AccountService implements UserDetailsService {

    AccountRep accountRep;
    AccountMapper accountMapper;
    private PasswordEncoder encoder;
    LogService logService;
    LogRep logRep;

    public Account saveNewAccount(AccountSignUpDTO accountSignUpDTO) {
        if (accountRep.findUserByEmailIgnoreCase(accountSignUpDTO.getEmail()).isPresent()) {
            throw new BadRequestException("User exist!");
        }
        Account account = accountMapper.toAccountFromAccountSignUpDTO(accountSignUpDTO, encoder);
        if (accountRep.count() == 0) {
            account.addRole("ROLE_ADMINISTRATOR");
        } else {
            account.addRole("ROLE_USER");
        }
        logService.saveLog(LogEvents.CREATE_USER, "Anonymous", accountSignUpDTO.getEmail().toLowerCase(),
                "api/auth/signup");
        return accountRep.save(account);
    }

    public Account findAccountByEmail(String email) {
        return accountRep.findUserByEmailIgnoreCase(email).orElseThrow(AccountNotFoundException::new);
    }

    public Account setPassword(NewPasswordDTO newPasswordDTO, UserPrincipal user) {

        if (encoder.matches(newPasswordDTO.getNew_password(), user.getPassword())) {
            throw new BadRequestException("The passwords must be different!");
        }

        Account account = findAccountByEmail(user.getUsername());
        account.setPassword(encoder.encode(newPasswordDTO.getNew_password()));
        accountRep.save(account);
        logService.saveLog(LogEvents.CHANGE_PASSWORD, account.getEmail(), account.getEmail().toLowerCase(), "api/auth/changepass");
        return account;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> account = accountRep.findUserByEmailIgnoreCase(email);
        if (account.isEmpty()) {
            throw new UsernameNotFoundException("Not found: " + email);
        }
        return new UserPrincipal(account.get());
    }
}
