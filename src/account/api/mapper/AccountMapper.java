package account.api.mapper;

import account.api.dto.ou.*;
import account.api.dto.in.AccountSignUpDTO;
import account.domain.entity.Account;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toAccountFromAccountSignUpDTO(AccountSignUpDTO accountSignUpDTO, PasswordEncoder encoder) {
        return Account.builder()
                .email(accountSignUpDTO.getEmail().toLowerCase())
                .name(accountSignUpDTO.getName())
                .lastName(accountSignUpDTO.getLastname())
                .password(encoder.encode(accountSignUpDTO.getPassword()))
                .accountNonLocked(true)
                .build();
    }

    public AccountDTO toAccountDTOFromAccount(Account account) {
        return AccountDTO.builder()
                .name(account.getName())
                .email(account.getEmail())
                .id(account.getId())
                .lastname(account.getLastName())
                .roles(account.getRoles())
                .build();
    }

    public AccountWithRolesDTO toAccountWithRolesDTOFromAccount(Account account) {
        return AccountWithRolesDTO.builder()
                .name(account.getName())
                .email(account.getEmail().toLowerCase())
                .id(account.getId())
                .lastname(account.getLastName())
                .roles(account.getRoles())
                .build();
    }

    public NewPasswordRespondDTO toNewPasswordRespondDTO(Account account) {
        return NewPasswordRespondDTO.builder()
                .email(account.getEmail().toLowerCase())
                .status("The password has been updated successfully")
                .build();
    }
}
