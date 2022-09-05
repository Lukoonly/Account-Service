package account.api.controller;

import account.api.dto.in.AccessOperationDTO;
import account.api.dto.in.RoleOperationDTO;
import account.api.dto.ou.AccountWithRolesDTO;
import account.api.dto.ou.DeleteStatusDTO;
import account.api.dto.ou.StatusDTO;
import account.api.mapper.AccountMapper;
import account.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@RequestMapping("/api/admin/user")
@RestController
public class AdminController {

    AdminService adminService;
    AccountMapper accountMapper;

    @PutMapping("/role")
    public AccountWithRolesDTO setTheRole(@Valid @RequestBody RoleOperationDTO roleOperationDTO) {
        return accountMapper.toAccountWithRolesDTOFromAccount(adminService.updateRole(roleOperationDTO));
    }

    @Transactional
    @DeleteMapping("/{email}")
    public DeleteStatusDTO deleteUser(@PathVariable String email) {
        adminService.deleteAccountByEmail(email);
        return new DeleteStatusDTO(email, "Deleted successfully!");
    }

    @GetMapping("/")
    public List<AccountWithRolesDTO> getUsersInformation() {
        return adminService.getAccounts().stream()
                .map(acc -> accountMapper.toAccountWithRolesDTOFromAccount(acc))
                .collect(Collectors.toList());
    }

    @PutMapping("/access")
    public StatusDTO setTheAccountActivity(@RequestBody AccessOperationDTO accountActivity) {
        String status = String.format("User %s %s!",
                accountActivity.getUser().toLowerCase(), accountActivity.getOperation().toLowerCase() + "ed");
        adminService.setTheAccountActivity(accountActivity);
        return StatusDTO.builder().status(status).build();
    }
}