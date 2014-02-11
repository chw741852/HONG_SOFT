package com.hong.core.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: Cai
 * Date: 13-12-12
 * Time: 下午1:36
 * To change this template use File | Settings | File Templates.
 */
@Aspect
public class LogAspectJ {
    private final static Log log = LogFactory.getLog(LogAspectJ.class);

    @Pointcut("execution(public * com.hong..*.controller.*Controller.*(..))")
    private void anyMethod(){ }

    @Before("anyMethod()")
    public void beforeAdvice(JoinPoint joinPoint) {
        log.info("前置通知：" + getMethodName(joinPoint) + " 方法开始执行！");
    }

//    @AfterReturning("anyMethod()")
//    public void afterReturningAdvice(JoinPoint joinPoint) {
//        log.info("后置通知：" + getMethodName(joinPoint) + " 方法执行正常结束！");
//    }

    @AfterThrowing(pointcut = "anyMethod()", throwing = "e")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception e) {
        StackTraceElement[] traces = e.getStackTrace();
        log.error("异常通知：" + getMethodName(joinPoint) + " 方法抛出异常：" + e.getMessage());
        if (traces != null && traces.length > 0) {
            for (StackTraceElement trace:traces) {
                if (trace.getMethodName().equals(joinPoint.getSignature().getName())) {
                    log.error(trace.toString());
                    break;
                }
            }
            e.printStackTrace();
        }
    }

    @After("anyMethod()")
    public void afterAdvice(JoinPoint joinPoint) {
        log.info("最终通知：" + getMethodName(joinPoint) + " 方法执行结束！");
    }

    @Around("anyMethod()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable{
        long beginTime = System.currentTimeMillis();
        // 传递给连接点对象进行接力处理
        Object result = pjp.proceed();
        long endTime = System.currentTimeMillis();
        log.info("环绕通知：" + getMethodName(pjp) + " 方法耗时" + (endTime - beginTime) + "毫秒！");

        return result;
    }

    private String getMethodName(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
    }
}
