package com.sbilhbank.insur.aspect;

import com.sbilhbank.insur.entity.primary.Logging;
import com.sbilhbank.insur.service.log.LoggingService;
import com.sbilhbank.insur.utils.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * Aspect for logging execution of service and repository Spring components.
 * @author Ramesh Fadatare
 *
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Autowired
    private LoggingService loggingService;


    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut(
//            "within(@org.springframework.stereotype.Repository *)" +
//            " || " +
//            "within(@org.springframework.stereotype.Service *)" +
//            " || " +
            "within(@org.springframework.web.bind.annotation.RestController *)"
    )
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut(
//            "within(com.sbilhbank.com.kh.holdingbalance..*)" +
//            " || " +
//            "within(com.sbilhbank.com.kh.holdingbalance.service..*)" +
//            " || " +
            "within(com.sbilhbank.insur.controllers..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if(joinPoint!=null) {
            log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : e.getMessage());
            Logging logging = Logging
                    .builder()
                    .loggingId(UUID.randomUUID())
                    .classPackage(joinPoint.getSignature().getDeclaringTypeName())
                    .method(joinPoint.getSignature().getName())
                    .logDate(new Date())
                    .exception(e.toString())
                    .logType(Log.logEnum.ERROR.name())
                    .cause(e.getCause() != null ? e.getCause().toString() : "NULL")
                    .message(e.getMessage() != null ? e.getMessage() : "NULL")
                    .build();
            loggingService.createLog(logging);
        }
//        throw e;
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
            Logging  logging = Logging
                    .builder()
                    .loggingId(UUID.randomUUID())
                    .classPackage(joinPoint.getSignature().getDeclaringTypeName())
                    .method(joinPoint.getSignature().getName())
                    .logDate(new Date())
                    .logType(Log.logEnum.DEBUG.name())
                    .message(Arrays.toString(joinPoint.getArgs()))
                    .build();
            loggingService.createLog(logging);
        }
        try {
            Object result = joinPoint.proceed();
            Logging  logging = Logging
                    .builder()
                    .loggingId(UUID.randomUUID())
                    .classPackage(joinPoint.getSignature().getDeclaringTypeName())
                    .method(joinPoint.getSignature().getName())
                    .logDate(new Date())
                    .logType(Log.logEnum.INFO.name())
                    .message(result.toString())
                    .build();
            loggingService.createLog( logging);

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            Logging  logging = Logging
                    .builder()
                    .loggingId(UUID.randomUUID())
                    .classPackage(joinPoint.getSignature().getDeclaringTypeName())
                    .method(joinPoint.getSignature().getName())
                    .logDate(new Date())
                    .exception(e.toString())
                    .logType(Log.logEnum.ERROR.name())
                    .cause(e.getCause()!=null?e.getCause().toString():"NULL")
                    .message(e.getMessage())
                    .build();
            loggingService.createLog(logging);
            throw e;
        }
    }
}
