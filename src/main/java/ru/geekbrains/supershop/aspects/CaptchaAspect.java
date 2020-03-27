package ru.geekbrains.supershop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import ru.geekbrains.supershop.exceptions.WrongCaptchaCodeException;
import ru.geekbrains.supershop.persistence.pojo.ReviewPojo;

import javax.servlet.http.HttpSession;

@Slf4j
@Aspect
@Configuration
public class CaptchaAspect {
// проверяем правильность ввода Captcha

    // перехватчик событий
    // точка входа в метод
    @Pointcut("within(ru.geekbrains.supershop.controllers..*)")
    public void controllerLayer() {}

    @Before(value = "controllerLayer() && " + "args(reviewPojo, session, ..)", argNames = "reviewPojo, session")
    public void checkCaptcha(ReviewPojo reviewPojo, HttpSession session) throws WrongCaptchaCodeException {
        if (!reviewPojo.getCaptchaCode().equals(session.getAttribute("captchaCode"))) {
            throw new WrongCaptchaCodeException("Error!");
        }
    }

    @After("execution(* ru.geekbrains.supershop.utils.CaptchaGenerator..*(..))")
    public void captchaAllMethods(JoinPoint joinPoint) {
        // логирование завершивших выполнение методов по короткому имени
        log.info("Method {} has been executed successfully !", joinPoint.toShortString());
    }
}
