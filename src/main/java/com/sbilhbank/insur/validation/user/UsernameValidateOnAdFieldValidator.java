package com.sbilhbank.insur.validation.user;

import com.sbilhbank.insur.entity.primary.UserLdap;
import com.sbilhbank.insur.repository.primary.UserLdapRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

@Configuration
public class UsernameValidateOnAdFieldValidator implements ConstraintValidator<UsernameValidateOnAdField, Object> {
    private static final UsernameValidateOnAdFieldValidator holder = new UsernameValidateOnAdFieldValidator();
    private UserLdapRepository userLdapRepository;
    private String field;
    private final String username= "username";

    @Bean
    public static UsernameValidateOnAdFieldValidator beanValidateOnAdField(UserLdapRepository userLdapRepository) {
        holder.userLdapRepository = userLdapRepository;
        return holder;
    }

    @Override
    public void initialize(UsernameValidateOnAdField constraintAnnotation) {
        this.field = constraintAnnotation.field();
    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Field f = ReflectionUtils.findField(value.getClass(), field);
        ReflectionUtils.makeAccessible(f);
        Field usernameField = ReflectionUtils.findField(value.getClass(), username);
        ReflectionUtils.makeAccessible(usernameField);
        Object fieldObj = null;
        String usernameObj = null;
        try {
            if (f.get(value).getClass().equals(Boolean.class)){
                return ! (Boolean)f.get(value);
            }
            usernameObj = (String) usernameField.get(value);
            if(!StringUtils.hasText(usernameObj)) return false;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        LdapQuery ldapQuery = LdapQueryBuilder.query()
                .countLimit(1)
                .where("objectclass").is("user")
                .and("objectcategory").is("person")
                .and("sAMAccountName").is(usernameObj);
        UserLdap userLdap = holder.userLdapRepository.findOne(ldapQuery).orElse(null);
        if (userLdap==null) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(username)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
