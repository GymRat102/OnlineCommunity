package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class AlphaAspect {

    // 切点筛选连接点
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut(){

    }

    @Before("pointcut()") // 以此为切点，针对这些连接点
    public void before(){
        System.out.println("before");
    }

    @After("pointcut()") // 以此为切点，针对这些连接点
    public void after(){
        System.out.println("after");
    }

    @AfterReturning("pointcut()") // 以此为切点，针对这些连接点
    public void afterReturning(){
        System.out.println("afterReturning");
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing(){
        System.out.println("afterThrowing");
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        Object obj = joinPoint.proceed();// 调用目标组件(连接点)的方法
        System.out.println("around after");
        return obj; // obj是目标组件的返回值
    }
}
