package account.api.dto.in;

import account.api.validator.BreachedPasswordConstraint;
import account.api.validator.LengthPassword;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
public class AccountSignUpDTO {
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastname;

    @NotEmpty
    @Pattern(regexp = ".*@acme.com$")
    private String email;
    @NotEmpty
    @LengthPassword
    @BreachedPasswordConstraint
    private String password;
}