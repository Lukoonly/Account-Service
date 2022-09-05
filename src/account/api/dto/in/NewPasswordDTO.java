package account.api.dto.in;

import account.api.validator.BreachedPasswordConstraint;
import account.api.validator.LengthPassword;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class NewPasswordDTO {
    @NotEmpty
    @LengthPassword
    @BreachedPasswordConstraint
    private String new_password;
}
