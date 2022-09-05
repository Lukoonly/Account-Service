package account.api.dto.ou;

import account.domain.entity.LogEvents;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class LogDTO {

    private Long id;
    private String date;
    private LogEvents action;
    private String subject;
    private String object;
    private String path;
}
