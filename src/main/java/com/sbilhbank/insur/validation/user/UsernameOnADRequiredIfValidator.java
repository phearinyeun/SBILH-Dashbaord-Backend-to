package com.sbilhbank.insur.validation.user;

import com.sbilhbank.insur.entity.primary.UserLdap;
import com.sbilhbank.insur.repository.primary.UserLdapRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

@Configuration
public class UsernameOnADRequiredIfValidator implements ConstraintValidator<UsernameOnADRequiredIf, Object> {
    private String field;
    private String verifyField;

    private static final UsernameOnADRequiredIfValidator holder = new UsernameOnADRequiredIfValidator();
    private UserLdapRepository userLdapRepository;

    @Bean
    public static UsernameOnADRequiredIfValidator beanUserRequiredIf(UserLdapRepository userLdapRepository) {
        holder.userLdapRepository = userLdapRepository;
        return holder;
    }
    @Override
    public void initialize(UsernameOnADRequiredIf constraintAnnotation) {
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
        if(fVerify.getType().equals(boolean.class.getClass())) {
            boolean isFVerify = (boolean) fieldObj;
            if (isFVerify) {
                LdapQuery ldapQuery = LdapQueryBuilder.query()
                        .countLimit(1)
                        .where("objectclass").is("user")
                        .and("objectcategory").is("person")
                        .and("sAMAccountName").is(verifyFieldObj.toString());
                UserLdap userLdap = holder.userLdapRepository.findOne(ldapQuery).orElse(null);
                return userLdap!=null;
            }
        }
        boolean matches = (fieldObj != null) && fieldObj.equals(verifyFieldObj);

        if (!matches) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("message")
                    .addPropertyNode(verifyField)
                    .addConstraintViolation();
        }

        return matches;
    }
}
