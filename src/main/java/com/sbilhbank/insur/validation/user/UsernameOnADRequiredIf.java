package com.sbilhbank.insur.validation.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameOnADRequiredIfValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameOnADRequiredIf {
    String message() default "{UsernameOnADRequiredIf.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field();

    String verifyField();
}
