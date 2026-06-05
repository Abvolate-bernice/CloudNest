package com.bbj.lease.web.admin.custom.interceptor;

import com.bbj.lease.common.login.LoginUser;
import com.bbj.lease.common.login.LoginUserHolder;
import com.bbj.lease.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


//要在自定义的WebMVC中注册拦截器 并设置拦截路径参数

//拦截处理请求，在每一次请求发送之前要检查token是否合法
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从请求头中拿出token
    String token = request.getHeader("access-token");

    Claims claims = JwtUtil.paraseToken(token);
    Long userId = claims.get("userId", Long.class);
    String username = claims.get("username", String.class);
    LoginUserHolder.setLoginUser(new LoginUser(userId, username));

    return true;

}

@Override
public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    LoginUserHolder.clear();
}

}

