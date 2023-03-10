package com.sbilhbank.insur.validation.unique;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

@Transactional
@Configuration
@Slf4j
public class UniqueKeyValidator implements ConstraintValidator<UniqueKey, Object> {
    @Autowired
    private SessionFactory sessionFactory;
    private CriteriaBuilder getCriteriaBuilder() {
        return sessionFactory.openSession().getCriteriaBuilder();
    }
    private String columnName;
    private Class<?> entityClass;
    private Class<?> entityClassMain;
    private CriteriaQuery getOriginalObject(Object value) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery criteria = criteriaBuilder.createQuery(entityClassMain);
        Root entityObject = criteria.from(entityClassMain);
        Predicate likeRestriction = null;
        try {
            Field field = value.getClass().getDeclaredField(this.columnName);
            field.setAccessible(true);
            Field fieldId = value.getClass().getDeclaredField("id");
            fieldId.setAccessible(true);
            String fieldValue = field.get(value).toString();
            Long fieldIdValue = Long.parseLong(Optional.ofNullable(fieldId.get(value)).orElse("0").toString());
            Optional<String> joinFieldName = Arrays.stream(
                    value
                            .getClass()
                            .getDeclaredFields()
                    )
                    .filter(s -> s.getType().equals(this.entityClass))
                    .map(s -> s.getName().toString())
                    .findFirst();
            if(joinFieldName.isEmpty()) {
                likeRestriction = criteriaBuilder.and(
                        criteriaBuilder.equal(entityObject.get("id"), fieldIdValue),
                        criteriaBuilder.equal(entityObject.get(this.columnName),fieldValue)
                );
                criteria.where(likeRestriction);
                return criteria;
            }
            Join<Object,Object> join = entityObject.join(joinFieldName.get(), JoinType.INNER);
            likeRestriction = criteriaBuilder.and(
                    criteriaBuilder.equal(entityObject.get("id"), fieldIdValue),
                    criteriaBuilder.equal(join.get(this.columnName),fieldValue)
            );
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        criteria.where(likeRestriction);
        return criteria;
    }

    private CriteriaQuery getDuplicate(Object value) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery criteria = criteriaBuilder.createQuery(entityClass);
        Root entityObject = criteria.from(entityClass);
        Predicate likeRestriction = null;
        try {
            Field field = value.getClass().getDeclaredField(this.columnName);
            field.setAccessible(true);
            String fieldValue = field.get(value).toString();
            likeRestriction = criteriaBuilder.and(
                    criteriaBuilder.equal(entityObject.get(this.columnName), fieldValue)
            );
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        criteria.where(likeRestriction);
        return criteria;
    }

    @Override
    public void initialize(UniqueKey constraintAnnotation) {
        this.columnName = constraintAnnotation.columnName();
        this.entityClass = constraintAnnotation.className();
        this.entityClassMain = constraintAnnotation.classMain();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        log.info("class: ", entityClassMain.toString());
        CriteriaQuery criteriaQueryOriginalObj = getOriginalObject(value);
        CriteriaQuery criteriaQueryValue = getDuplicate(value);
        boolean isValidOri = sessionFactory.createEntityManager().createQuery(criteriaQueryOriginalObj).getResultList().size() > 0;
        boolean isValidHasValue = sessionFactory.createEntityManager().createQuery(criteriaQueryValue).getResultList().size() > 0;
        if (!isValidOri) {
            if(isValidHasValue){
                context.disableDefaultConstraintViolation();;
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(this.columnName)
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}