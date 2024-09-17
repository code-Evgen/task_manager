package ru.tatarinov.taskmanager.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.tatarinov.taskmanager.exception.AuthorizationFailException;
import ru.tatarinov.taskmanager.service.AuthorizationService;

@Aspect
@Component
public class AuthorizationAOP {
    private final AuthorizationService authorizationService;

    public AuthorizationAOP(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) && @annotation(Authorization) && execution(public * ru.tatarinov.taskmanager.controller.*Controller.*(int,..))")
    public void isTaskAuthenticationRequired(){
    }


    @Before("isTaskAuthenticationRequired() " +
            "&& @annotation(authorization)" +
            "&& args(taskId,..)")
    public void ownerAuthorizationBefore(int taskId, Authorization authorization){
        if (authorization.type().equals(AuthorizationType.OWNER)) {
            boolean authorized = authorizationService.ownerAuthorization(taskId);
            if (!authorized)
                throw new AuthorizationFailException("Not enough rights for this task");
        }

        if (authorization.type().equals(AuthorizationType.BOTH)) {
            boolean authorizedOwner = authorizationService.ownerAuthorization(taskId);
            boolean authorizedExecutor = authorizationService.executorAuthorization(taskId);
            if (!(authorizedOwner || authorizedExecutor))
                throw new AuthorizationFailException("Not enough rights for this task");
        }
    }
}
