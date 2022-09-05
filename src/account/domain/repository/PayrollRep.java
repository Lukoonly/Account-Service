package account.domain.repository;

import account.domain.entity.Payroll;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRep extends CrudRepository<Payroll, Long> {

    Optional<Payroll> findPayrollByEmployeeIgnoreCaseAndPeriod(String employee, String period);

    List<Payroll> findPayrollByEmployeeIgnoreCaseOrderByPeriodDesc(String employee);
}
