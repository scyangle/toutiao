package com.scy.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.scy.controller.*.*(..))")
    public void beforeMethod() {
        logger.info("===Before====");
    }

    @After("execution(* com.scy.controller.*.*(..))")
    public void afterMethod() {
        logger.info("===After====");
    }

    @Before("@annotation(needTest)")
    public void needTest(NeedTest needTest) {
        if (true == needTest.value()) {
            System.out.println("This is test controller .");
        }
    }

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        logger.info(joinPoint.getSignature() + " executed in " + executionTime + " ms");
        return proceed;
    }

}
