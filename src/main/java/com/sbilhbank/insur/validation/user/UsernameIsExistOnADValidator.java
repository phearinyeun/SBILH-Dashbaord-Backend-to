package com.sbilhbank.insur.validation.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameIsExistOnADValidate.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameIsExistOnADValidator {
    String message() default "Username doesn't exist on AD!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}