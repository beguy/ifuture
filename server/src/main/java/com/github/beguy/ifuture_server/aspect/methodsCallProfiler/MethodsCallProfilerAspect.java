package com.github.beguy.ifuture_server.aspect.methodsCallProfiler;

import com.github.beguy.ifuture_server.aspect.Switchable;
import com.github.beguy.ifuture_server.aspect.methodsCallProfiler.mbean.MethodsCallProfilerMXBean;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Aspect
@Configuration
public class MethodsCallProfilerAspect {
    private final Switchable profilerState;
    private final MethodsCallCounters methodsCallCounters;

    @Autowired
    public MethodsCallProfilerAspect(MethodsCallProfilerMXBean profilerMXbean,
                                     @Lazy MethodsCallCounters methodsCallCounters) {
        this.profilerState = profilerMXbean;
        this.methodsCallCounters = methodsCallCounters;
    }


    @Pointcut("execution(void com.github.beguy.ifuture_server.service.AccountService.addAmount(..)) ||" +
            "execution(Long com.github.beguy.ifuture_server.service.AccountService.getAmount(..)) ")
    private void serviceMethod() {
    }

    @Around("serviceMethod()")
    public Object logWebServiceCall(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        Object result = thisJoinPoint.proceed();
        if (profilerState.isEnabled()) {
            String methodName = thisJoinPoint.getSignature().toString();
            methodsCallCounters.increment(methodName);
        }
        return result;
    }
}
