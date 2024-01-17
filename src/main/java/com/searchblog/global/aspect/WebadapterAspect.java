package com.searchblog.global.aspect;

import com.searchblog.global.dto.SelfValidating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
@Slf4j
@Aspect
@Component(value = "WebAdapterAspect")
public class WebadapterAspect {
    @Before("execution(* com.searchblog.api.adpater.in.web.*Controller.*Run(..))")
    public void before(JoinPoint joinPoint) {
        log.info("[Before] => {}", joinPoint.getSignature().getName());
        log.debug("CommandDTO Validation 검증 시작.");
        for(Object obj : joinPoint.getArgs()){
            if(Boolean.TRUE.equals(obj instanceof SelfValidating<?>)){
                ((SelfValidating<?>) obj).validateSelf();
                log.info("Validation 검증 성공 !! \n[{}]", obj);
            }
        }
    }


}
