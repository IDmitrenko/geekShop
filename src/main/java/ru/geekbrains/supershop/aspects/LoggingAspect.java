package ru.geekbrains.supershop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Aspect
@Configuration
public class LoggingAspect {

    @After("execution(* ru.geekbrains.supershop.services..*(..))")
    public void profileAllMethods(JoinPoint joinPoint) {
        // логирование завершивших выполнение методов по короткому имени
        log.info("Method {} has been executed successfully !", joinPoint.toShortString());
    }

}