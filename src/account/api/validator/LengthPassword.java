package account.api.validator;


import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LengthPasswordValidator.class)
@Target(ElementType.FIELD)
@Documented
public @interface LengthPassword {
    String message() default "Password length must be 12 chars minimum!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
