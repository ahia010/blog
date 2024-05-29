package com.ahia.blog.security;

import com.ahia.blog.entity.R;
import com.ahia.blog.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.PrintWriter;

@Aspect
@Component
public class AuthenticationAspect {
    @Around("@annotation(Authentication)")
    public Object checkPermissions(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Token"); // 从请求头中获取token
        if (token != null) {
//            System.out.println("token: " + token);
            Integer userRole = TokenUtil.extractRole(token);
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Authentication annotation = signature.getMethod().getAnnotation(Authentication.class);
            int[] requiredRoles = annotation.role();


            for (int requiredRole : requiredRoles) {
                if (userRole >= requiredRole) {
                    return joinPoint.proceed(); // 角色验证通过，继续执行目标方法
                }
            }
        }
        R errorResponse = R.error(403, "用户身份过期，请重新登录");
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setCharacterEncoding("UTF-8"); // 设置字符编码为UTF-8
        response.setContentType("application/json;charset=UTF-8"); // 设置响应类型和字符编码

        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(errorResponse));
        writer.flush();
//        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.setContentType("application/json");
//        response.getWriter().write(errorResponse.toString());
//        response.getWriter().flush();
        return null; // 角色验证不通过，返回 null
    }
}
