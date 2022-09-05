package account.api.mapper;

import account.api.dto.in.PayrollDTO;
import account.api.dto.ou.ResponseAccountForPeriodDTO;
import account.domain.entity.Payroll;
import org.springframework.stereotype.Component;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class PayrollMapper {
    public ResponseAccountForPeriodDTO toResponseAccountForPeriodDTOFromAccount(Payroll payroll) {
        return ResponseAccountForPeriodDTO.builder()
                .period(payroll.getLocalDate().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.US) + "-" + payroll.getLocalDate().getYear())
                .lastname(payroll.getAccount().getLastName())
                .name(payroll.getAccount().getName())
                .salary(String.format("%d dollar(s) %d cent(s)", payroll.getSalary() / 100, payroll.getSalary() % 100))
                .build();
    }

    public Payroll toPayrollFromPayrollDTO(PayrollDTO payrollDTO) {
        return Payroll.builder()
                .employee(payrollDTO.getEmployee())
                .period(payrollDTO.getPeriod())
                .salary(payrollDTO.getSalary())
                .build();
    }

    public List<Payroll> toPayrollListFromPayrollListDTO(List<PayrollDTO> list) {
        return list.stream().map(this::toPayrollFromPayrollDTO).collect(Collectors.toList());
    }
}
