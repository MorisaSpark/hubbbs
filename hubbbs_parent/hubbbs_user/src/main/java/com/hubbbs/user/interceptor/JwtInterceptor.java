package com.hubbbs.user.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2018/12/6
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        response.setHeader("Access-Control-Allow-Origin","*");
//        response.setHeader("Access-Control-Allow-Headers", "*");
//        response.setHeader("Access-Control-Allow-Methods", "*");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
        System.out.println("经过拦截器");
        String authorization = request.getHeader("Authorization");
        System.out.println("authorization===="+authorization);
        //有令牌
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try {
                Claims claims = jwtUtil.parseJWT(token);

                if (claims != null) {
                    request.setAttribute("claims_" + claims.get("roles"), token);
                    request.setAttribute("accountId", claims.getId());
                    request.setAttribute("accountUsername", claims.getSubject());
                    request.setAttribute("roles", claims.get("roles"));
                    request.setAttribute("userId", claims.get("userId"));
                }
            } catch (Exception e) {
                System.out.println(e);
                throw new RuntimeException("令牌错误");//如果用户令牌解析错误，可能是用户瞎写的
            }
        }
        return true;
    }
}
