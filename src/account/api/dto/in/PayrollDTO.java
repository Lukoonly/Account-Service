package account.api.dto.in;

import lombok.Getter;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
public class PayrollDTO {

    @NotBlank(message = "Employee cannot be empty")
    private String employee;

    @NotBlank(message = "Period cannot be empty")
    private String period;

    @Min(value = 0, message = "Salary must be non negative!")
    private Long salary;
}