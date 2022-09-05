package account.api.dto.ou;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class DeleteStatusDTO {
    private String user;
    private String status;
}
