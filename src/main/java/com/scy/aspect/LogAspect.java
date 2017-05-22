package com.scy.aspect;

import org.aspectj.lang.annotation.After;
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
    public void beforeMethod(){
        logger.info("===Before====");
    }
    @After("execution(* com.scy.controller.*.*(..))")
    public void afterMethod(){
        logger.info("===After====");
    }
}
