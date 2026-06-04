package com.snb.ms.auth.authorization;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

  private final AuthorizationChecker authorizationChecker;

  @Around("@annotation(requirePrivilege)")
  public Object authorize(ProceedingJoinPoint joinPoint, RequirePrivilege requirePrivilege)
      throws Throwable {
    authorizationChecker.ensureHasPrivilege(requirePrivilege.value());
    return joinPoint.proceed();
  }
}
