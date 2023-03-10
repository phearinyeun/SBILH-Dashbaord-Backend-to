package com.sbilhbank.insur.validation.requiredif;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RequiredIfValidator.class)
@Documented
public @interface RequiredIf {
    String message() default "{RequiredIf.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field();

    String verifyField();
}
