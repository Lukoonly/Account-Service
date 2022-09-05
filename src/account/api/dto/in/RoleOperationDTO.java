package account.api.dto.in;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class RoleOperationDTO {
    @NotBlank(message = "User cannot be empty")
    private String user;
    private String role;
    private String operation;
}