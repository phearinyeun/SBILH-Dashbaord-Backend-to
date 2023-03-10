package com.sbilhbank.insur.validation.compare;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CompareValidator.class)
@Documented
public @interface Compare {
    String message() default "{Compare.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field();

    String verifyField();
}
