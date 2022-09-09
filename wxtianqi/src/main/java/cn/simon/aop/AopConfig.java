package cn.simon.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author ：Simon
 * @date ：Created in 2022/9/7 1:40
 * @description：
 * @modified By：
 * @version: v1.0
 */
@Aspect
@Component
public class AopConfig {



    @Pointcut("execution(* cn.simon.timer.TimePush.pushTianQi(..))")
    public void test(){

    }

    public Object doAround(ProceedingJoinPoint joinPoint){
        Object proceed = null;
        try {

            // run
            proceed = joinPoint.proceed();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return proceed;
    }

}
