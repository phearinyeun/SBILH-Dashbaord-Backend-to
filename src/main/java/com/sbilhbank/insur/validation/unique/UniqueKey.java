package com.sbilhbank.insur.validation.unique;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueKeyValidator.class)
@Documented
public @interface UniqueKey {
    String columnName();
    Class<?> className();
    Class<?> classMain();
    String message() default "{UniqueKey.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}