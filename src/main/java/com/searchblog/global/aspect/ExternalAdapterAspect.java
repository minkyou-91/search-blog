//package com.searchblog.global.aspect;
//
//import com.searchblog.api.domain.SearchBlog;
//import com.searchblog.global.factory.ExternalPortFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Aspect
//@Component(value = "ExternalAdapterAspect")
//public class ExternalAdapterAspect {
//    private ExternalPortFactory externalPortFactory;
//    @AfterThrowing(pointcut = "execution(* com.searchblog..adapter.out.external.*ExternalAdapter.*Search(..))", throwing = "e")
//    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
//        log.error("[BlogSearchService AfterThrowing] => {} [{}] => {}", joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage());
//        this.afterThrowingForExternal(joinPoint,e, externalPortFactory);
//    }
//
//}
