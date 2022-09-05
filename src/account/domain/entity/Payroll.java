package account.domain.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Employee cannot be empty")
    private String employee;

    @NotBlank(message = "Period cannot be empty")
    private String period;

    @Min(value = 0, message = "Salary must be non negative!")
    private Long salary;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    LocalDate localDate;
}