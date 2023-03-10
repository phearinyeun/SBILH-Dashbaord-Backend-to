package com.sbilhbank.insur.validation.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameIsExistOnDBValidate.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameIsExistOnDBValidator {
    String message() default "Username is exist!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}