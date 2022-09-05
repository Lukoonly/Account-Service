package account.api.dto.ou;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class AccountWithRolesDTO {
    private long id;
    private String name;
    private String lastname;
    private String email;
    private List<String> roles;
}
