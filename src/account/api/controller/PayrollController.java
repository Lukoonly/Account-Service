
package account.api.controller;

import account.api.dto.in.PayrollDTO;
import account.api.dto.ou.StatusDTO;
import account.api.mapper.AccountMapper;
import account.api.mapper.PayrollMapper;
import account.domain.entity.Payroll;
import account.service.PayrollService;
import account.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@RequestMapping("/api")
@RestController
public class PayrollController {

    PayrollService payrollService;
    AccountMapper accountMapper;
    PayrollMapper payrollMapper;


    @PostMapping("/acct/payments")
    public StatusDTO postPayments(@Valid @RequestBody List<PayrollDTO> list) {
        payrollService.addNewPayrolls(list);
        return new StatusDTO("Added successfully!");
    }

    @PutMapping("/acct/payments")
    public StatusDTO putPayment(@Valid @RequestBody PayrollDTO payrollDTO) {
        payrollService.putPayroll(payrollDTO);
        return new StatusDTO("Updated successfully!");
    }

    @GetMapping("/empl/payment")
    public Optional<Object> getPaymentsByPeriod(@RequestParam Optional<String> period
            , @AuthenticationPrincipal UserPrincipal user) {
        List<Payroll> list = payrollService.getPayrollsByPeriod(period, user.getUsername());
        return list.size() == 1 ?
                Optional.of(payrollMapper.toResponseAccountForPeriodDTOFromAccount(list.get(0))) :
                Optional.of(list.stream().map(payroll -> payrollMapper.toResponseAccountForPeriodDTOFromAccount(payroll))
                        .collect(Collectors.toList()));
    }
}