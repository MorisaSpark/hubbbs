package com.hubbbs.user.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2019/4/1
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@Component
@Aspect
public class AuthorityAop {
    @Autowired
    private HttpServletRequest request;

    //匹配com.hubbbs.user.controller.AccountController包内的任意参数的方法
    @Pointcut("(execution(* com.hubbbs.user.controller.AccountController.add(..)))||(execution(* com.hubbbs.user.controller.UserController.delete(..)))||(execution(* com.hubbbs.user.controller.AccountController.delete(..)))")
    public void executeService() {

    }

    /**
     * 环绕通知：
     * 环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
     * 环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     */
    @Around("executeService()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("环绕通知的目标方法名：" + proceedingJoinPoint.getSignature().getName());
        try {//obj之前可以写目标方法执行前的逻辑
            String admin = (String) request.getAttribute("claims_admin");
            System.out.println("admin====" + admin);
            if (admin == null || "".equals(admin)) {
                throw new RuntimeException("权限不足");
            }
            Object obj = proceedingJoinPoint.proceed();//调用执行目标方法
            return obj;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
