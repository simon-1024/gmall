package com.simon.gmall.interceptors;

import com.simon.gmall.annotations.LoginRequired;
import com.simon.gmall.util.CookieUtil;
import com.simon.gmall.util.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //拦截代码，判断被拦截的请求的访问方法的注解（是否需要被拦截）
        HandlerMethod hm = (HandlerMethod) handler;
        LoginRequired methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);

        //得到请求来源的URl
        StringBuffer url = request.getRequestURL();
        System.out.println("请求来源是==>"+url);


        //该方法是否加入了拦截注解,即是否需要被拦截
        if (methodAnnotation == null) {
            //没有加如拦截注解,直接放行
            return true;

        }

        String token = "";

        //以下操作是为了判断是否已经登录，对比token的真伪
        // 尝试取出本地cookie里存储的token
        String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);

        if (StringUtils.isNotBlank(oldToken)) {
            token = oldToken;
        }

        String newToken = request.getParameter("token");

        if (StringUtils.isNotBlank(newToken)) {
            token = newToken;
        }


        //是否必须登录
        boolean loginSuccess = methodAnnotation.loginSuccess();

        //调用认证中心进行验证
        String success = "fail";
        if (StringUtils.isNotBlank(token)) {
            success = HttpclientUtil.doGet("http://127.0.0.1:8087/verify?token=" + token);

        }


        if (loginSuccess) { //需要登录

            //必须登录成功才能操作
            if (!success.equals("success")) {
                //重定向到认证中心进行登录
                StringBuffer requestURL = request.getRequestURL();
                response.sendRedirect("http://127.0.0.1:8087/index?ReturnUrl=" + requestURL);
                return false;
            }

            //需要将token携带的用户信息写入
            request.setAttribute("memberId", "1");
            request.setAttribute("nickname", "simon");

            //验证通过，覆盖cookie中token
            if (StringUtils.isNotBlank(token)) {
                CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
            }
        } else {  //不需要登录也能操作，但是必须验证

            if (success.equals("success")) {  //验证成功
                request.setAttribute("memberId", "1");
                request.setAttribute("nickname", "simon");

                //通过验证，覆盖cookie中的token
                if (StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
                }
            }
        }
        return true;
    }
}
