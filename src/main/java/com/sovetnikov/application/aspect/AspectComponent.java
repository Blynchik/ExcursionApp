package com.sovetnikov.application.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AspectComponent {
    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        log.info(joinPoint.getSignature() + " executed in " + executionTime + "ms");
        return proceed;
    }

    @AfterReturning(pointcut = "execution(* com.sovetnikov.application.*.*(*))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info(joinPoint.getSignature().getName() + "result = " + result.toString());
    }
}
