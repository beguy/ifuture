package com.github.beguy.ifuture_server.aspect;

import com.github.beguy.ifuture_server.mbean.KeyCounter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Aspect
@Configuration
public class ServiceLoadProfilerAspect implements Switchable {
    private final static Log log = LogFactory.getLog(ServiceLoadProfilerAspect.class);
    private boolean enabled = false;

    private final KeyCounter<String, Long> profilerCounter;

    @Autowired
    @Lazy
    public ServiceLoadProfilerAspect(@Qualifier("serviceLoadProfiler") KeyCounter<String, Long> profilerCounter) {
        this.profilerCounter = profilerCounter;
    }

    @Pointcut("execution(void com.github.beguy.ifuture_server.service.AccountService.addAmount(..)) ||" +
            "execution(Long com.github.beguy.ifuture_server.service.AccountService.getAmount(..)) ")
    private void serviceMethod() {
    }

    @Around("serviceMethod()")
    public Object logWebServiceCall(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        Object result = thisJoinPoint.proceed();
        if (isEnabled()) {
            String methodName = thisJoinPoint.getSignature().toString();
            profilerCounter.increment(methodName);
        }
        return result;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
