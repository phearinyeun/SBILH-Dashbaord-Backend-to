package com.sbilhbank.insur.validation.compare;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

@Configuration
public class CompareValidator implements ConstraintValidator<Compare, Object> {
    private String field;
    private String verifyField;

    @Override
    public void initialize(Compare constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.verifyField = constraintAnnotation.verifyField();
    }
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Field f = ReflectionUtils.findField(value.getClass(), field);
        ReflectionUtils.makeAccessible(f);
        Field fVerify = ReflectionUtils.findField(value.getClass(), verifyField);
        ReflectionUtils.makeAccessible(fVerify);
        Object fieldObj = null;
        Object verifyFieldObj = null;
        try {
            fieldObj = f.get(value);
            verifyFieldObj = fVerify.get(value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        boolean neitherSet = (fieldObj == null) && (verifyFieldObj == null);

        if (neitherSet) {
            return true;
        }

        boolean matches = (fieldObj != null) && fieldObj.equals(verifyFieldObj);

        if (!matches) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(verifyField)
                    .addConstraintViolation();
        }

        return matches;
    }
}
