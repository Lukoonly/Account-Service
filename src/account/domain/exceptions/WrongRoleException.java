package account.domain.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class WrongRoleException extends RuntimeException {

    public WrongRoleException(String message) {
        super(message);
    }
}
