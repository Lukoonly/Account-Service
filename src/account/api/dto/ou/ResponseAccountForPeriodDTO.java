package account.api.dto.ou;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ResponseAccountForPeriodDTO {
    private String name;
    private String lastname;
    private String period;
    private String salary;
}