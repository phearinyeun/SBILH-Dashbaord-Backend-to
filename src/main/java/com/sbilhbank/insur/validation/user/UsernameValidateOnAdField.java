package com.sbilhbank.insur.validation.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidateOnAdFieldValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameValidateOnAdField {
    String message() default "Username doesn't exist on AD!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String field();
}
