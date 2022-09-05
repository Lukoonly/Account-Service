package account.service;

import account.api.dto.in.AccessOperationDTO;
import account.api.dto.in.RoleOperationDTO;
import account.api.mapper.AccountMapper;
import account.domain.entity.Account;
import account.domain.entity.LogEvents;
import account.domain.exceptions.AccountNotFoundException;
import account.domain.exceptions.BadRequestException;
import account.domain.repository.AccountRep;
import account.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class AdminService {

    AccountRep accountRep;
    AccountMapper accountMapper;
    LogService logService;


    public List<Account> getAccounts() {
        List<Account> result = new ArrayList<>();
        accountRep.findAll().forEach(result::add);
        return result;
    }

    public void deleteAccountByEmail(String email) {
        getAccountOrThrowExceptionIfUserNotExist(email);
        isAdministratorAccount(email, "Can't remove ADMINISTRATOR role!");
        accountRep.deleteAccountByEmail(email);
        logService.saveLog(LogEvents.DELETE_USER, getEmailOfAuthorizedUser(), email.toLowerCase(), "/api/admin/user");
    }

    public Account updateRole(RoleOperationDTO roleOperationDTO) {
        Account account = getAccountOrThrowExceptionIfUserNotExist(roleOperationDTO.getUser());
        return choseOperation(roleOperationDTO, account);
    }

    private Account choseOperation(RoleOperationDTO operation, Account account) {
        switch (operation.getOperation()) {
            case "GRANT":
                if (isCombiningRoles(account.getRole(), operation.getRole())) {
                    throw new BadRequestException("The user cannot combine administrative and business roles!");
                }
                checkIsRoleExists(operation.getRole());
                account.addRole(operation.getRole());
                logService.saveLog(LogEvents.GRANT_ROLE,
                        getEmailOfAuthorizedUser(),
                        String.format("Grant role %s to %s", operation.getRole().toUpperCase(), account.getEmail().toLowerCase()),
                        "/api/admin/user/role");
                return accountRep.save(account);
            case "REMOVE":
                checkIsRoleExists(operation.getRole());
                isAdministratorAccount(operation.getUser(), "Can't remove ADMINISTRATOR role!");

                if (!account.getRole().contains(operation.getRole())) {
                    throw new BadRequestException("The user does not have a role!");
                }

                if (account.getRoles().size() == 1) {
                    throw new BadRequestException("The user must have at least one role!");
                }
                account.removeRole(operation.getRole());
                logService.saveLog(LogEvents.REMOVE_ROLE,
                        getEmailOfAuthorizedUser(),
                        String.format("Remove role %s from %s", operation.getRole(), account.getEmail()),
                        "/api/admin/user/role");
                return accountRep.save(account);
            default:
                throw new BadRequestException("Wrong operation, please check your request");
        }
    }

    private String getEmailOfAuthorizedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((UserPrincipal) principal).getUsername();
    }

    private void checkIsRoleExists(String role) {
        if (!(role.equals("USER") || role.equals("ACCOUNTANT") || role.equals("ADMINISTRATOR") || role.equals("AUDITOR"))) {
            throw new AccountNotFoundException("Role not found!");
        }
    }

    private void isAdministratorAccount(String email, String message) {
        if (accountRep.findUserByEmailIgnoreCase(email).get().getRole().contains("ADMINISTRATOR")) {
            throw new BadRequestException(message);
        }
    }

    private Account getAccountOrThrowExceptionIfUserNotExist(String email) {
        return accountRep.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new AccountNotFoundException("User not found!"));
    }

    public void setTheAccountActivity(AccessOperationDTO accountActivity) {
        Account account = accountRep.findUserByEmailIgnoreCase(accountActivity.getUser())
                .orElseThrow(() -> new BadRequestException("Wrong user"));
        String operation = accountActivity.getOperation();
        String subject = getEmailOfAuthorizedUser();
        String object;
        String path = "/api/admin/user/access";
        switch (operation) {
            case "LOCK":
                if (account.getRole().contains("ADMINISTRATOR")) {
                    throw new BadRequestException("Can't lock the ADMINISTRATOR!");
                }
                object = String.format("Lock user %s", account.getEmail());
                account.setAccountNonLocked(false);
                logService.saveLog(LogEvents.LOCK_USER, subject, object, path);
                break;
            case "UNLOCK":
                object = String.format("Unlock user %s", account.getEmail());
                account.setAccountNonLocked(true);
                logService.saveLog(LogEvents.UNLOCK_USER, subject, object, path);
                break;
        }
        accountRep.save(account);
    }

    public boolean isCombiningRoles(String accountRole, String requestedRole) {
        return isCombiningAdminWithBusinessRole(accountRole, requestedRole) ||
                isCombiningBusinessRoleWithAdmin(accountRole, requestedRole);
    }

    public boolean isCombiningBusinessRoleWithAdmin(String accountRole, String requestedRole) {
        return accountRole.contains("ADMINISTRATOR") &&
                (requestedRole.contains("ACCOUNTANT") ||
                        requestedRole.contains("USER") ||
                        requestedRole.contains("AUDITOR"));
    }

    public boolean isCombiningAdminWithBusinessRole(String accountRole, String requestedRole) {
        return (accountRole.contains("USER") ||
                accountRole.contains("ACCOUNTANT") ||
                accountRole.contains("AUDITOR"))
                && requestedRole.contains("ADMINISTRATOR");
    }

}