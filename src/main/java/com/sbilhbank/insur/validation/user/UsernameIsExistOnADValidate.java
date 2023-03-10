package com.sbilhbank.insur.validation.user;

import com.sbilhbank.insur.entity.primary.UserLdap;
import com.sbilhbank.insur.repository.primary.UserLdapRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Configuration
public class UsernameIsExistOnADValidate implements ConstraintValidator<UsernameIsExistOnADValidator, String> {
    private static final UsernameIsExistOnADValidate holder = new UsernameIsExistOnADValidate();
    private UserLdapRepository userLdapRepository;

    @Bean
    public static UsernameIsExistOnADValidate beanUser(UserLdapRepository userLdapRepository) {
        holder.userLdapRepository = userLdapRepository;
        return holder;
    }

    @Override
    public void initialize(UsernameIsExistOnADValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        LdapQuery ldapQuery = LdapQueryBuilder.query()
                .countLimit(1)
                .where("objectclass").is("user")
                .and("objectcategory").is("person")
                .and("sAMAccountName").is(value);
        UserLdap userLdap = holder.userLdapRepository.findOne(ldapQuery).orElse(null);
        return userLdap!=null;
    }
}
