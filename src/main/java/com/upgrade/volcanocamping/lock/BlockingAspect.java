package com.upgrade.volcanocamping.lock;

import com.upgrade.volcanocamping.exceptions.MaxRetryLimitExceededException;
import com.upgrade.volcanocamping.service.ResourceLockService;
import lombok.extern.java.Log;
import org.aspectj.apache.bcel.generic.RET;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Aspect
@Component
@Log
public class BlockingAspect {

    private static final long INTERVAL = 1000L;
    private static final int RETRY = 3;
    private final ResourceLockService resourceLockService;

    @Autowired
    public BlockingAspect(ResourceLockService resourceLockService) {
        this.resourceLockService = resourceLockService;
    }


    @Around(value = "@annotation(resource)")
    public Object acquireLock(ProceedingJoinPoint pjp, BlockingResource resource) throws Throwable {
        log.info(String.format("Interception call to %s.%s - Resource Id : %s",
                pjp.getTarget().getClass(),
                pjp.getSignature().getName(),
                resource.resourceId()));
        int retryCount = 0;
        while(resourceLockService.isResourceLocked(resource.resourceId()) && retryCount <=RETRY) {
            Thread.sleep(INTERVAL);
            retryCount ++;
        }
        if(retryCount >= RETRY) {
            throw new MaxRetryLimitExceededException();
        }
        try {
            this.resourceLockService.acquire(resource.resourceId());
            return pjp.proceed();
        }
        catch (Exception ex) {
            log.info(ex.getMessage());
        }
        finally {
            this.resourceLockService.release(resource.resourceId());
            return null;
        }
    }

}
