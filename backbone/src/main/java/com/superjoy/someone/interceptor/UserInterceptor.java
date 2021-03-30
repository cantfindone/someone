package com.superjoy.someone.interceptor;

import cn.hutool.core.util.StrUtil;
import com.superjoy.someone.exception.CommonException;
import com.superjoy.someone.model.UserInfo;
import com.superjoy.someone.utils.JwtUtil;
import com.superjoy.someone.utils.UserContext;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Ping
 */
@Component
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    static final String basic = "Basic";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("getRequestURI:{}", request.getRequestURI());
        if (request.getRequestURI().contains("swagger")) {
            return true;
        }
        if (request.getRequestURI().equals("/auth/phone")) {
            return true;
        }
        String authorization = request.getHeader("Authorization");
        authorization = StrUtil.removePrefix(authorization, "bearer ");
        authorization = StrUtil.removePrefix(authorization, "Bearer ");
        if (StrUtil.isNotBlank(authorization) && !authorization.startsWith(basic)) {
            Claims claims = JwtUtil.decode(authorization);
            UserContext.setUser(UserInfo.builder().mobile(claims.getId()).userId(claims.getSubject()).build());
        } else {
            throw new CommonException("请求头缺失授权token", 401);
        }
        return false;
    }
}
