package cn.yun.oddworld.config;

import cn.yun.oddworld.dto.AuthRequest;
import cn.yun.oddworld.dto.BaseResult;
import cn.yun.oddworld.dto.UserAuthData;
import cn.yun.oddworld.feign.UserFeign;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    @Lazy
    private  UserFeign userFeign;
    @Autowired
    private  AuthProperties authProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            writeAuthFail(response);
            return false;
        }
        AuthRequest authRequest =new AuthRequest();
        authRequest.setToken(token);
        BaseResult<UserAuthData> userAuthDataBaseResult = userFeign.auth(authRequest);
        UserAuthData data = userAuthDataBaseResult.getData();
        if (data == null || !(Boolean.TRUE.equals(data.getValid()))) {
            writeAuthFail(response);
            return false;
        }
        request.setAttribute("userId", data.getUserId());
        return true;
    }

    private void writeAuthFail(HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"code\":100000,\"msg\":\"未授权或token无效\",\"data\":null}");
        writer.flush();
        writer.close();
    }
} 