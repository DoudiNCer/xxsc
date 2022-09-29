package com.sipc.xxsc.util.larkRobot;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
public class LarkAop {

    @Resource
    LarkRobot larkRobot;

    @Pointcut("execution(* *..controller.*.*(..))")
    public void lark() {

    }
    @AfterThrowing(pointcut = "lark()", throwing = "exception")
    public void larkRobot(JoinPoint point, Exception exception){
        if (larkRobot.getDev()) return;
        larkRobot.sendExceptionMessage(point, exception);
    }
}
