package account.api.controller;

import account.api.dto.ou.AccountDTO;
import account.api.dto.in.AccountSignUpDTO;
import account.api.dto.in.NewPasswordDTO;
import account.api.dto.ou.NewPasswordRespondDTO;
import account.api.mapper.AccountMapper;
import account.service.AccountService;
import account.service.PayrollService;
import account.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@RequestMapping("api/auth")
@RestController
public class UserController {

    AccountMapper accountMapper;
    AccountService accountService;
    PayrollService payrollService;

    @PostMapping("/signup")
    public AccountDTO returnNewAccount(@Valid @RequestBody AccountSignUpDTO accountSignUpDTO) {
        return accountMapper.toAccountDTOFromAccount(accountService.saveNewAccount(accountSignUpDTO));
    }

    @PostMapping("/changepass")
    public NewPasswordRespondDTO setNewPassword(@Valid @RequestBody NewPasswordDTO newPasswordDTO,
                                                @AuthenticationPrincipal UserPrincipal user) {
        return accountMapper.toNewPasswordRespondDTO(accountService.setPassword(newPasswordDTO, user));
    }
}