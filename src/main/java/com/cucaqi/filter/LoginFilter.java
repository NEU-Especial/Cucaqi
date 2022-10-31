package com.cucaqi.filter;

import com.alibaba.fastjson.JSON;
import com.cucaqi.controller.Result;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {
    //路径匹配器，支持大括号通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //设置不需要处理的请求路径
        String[] urls = {
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "front/**"
        };
        //获取当次请求的uri
        String requestURI = request.getRequestURI();
        boolean checkURI = checkURI(urls, requestURI);
        //查看本次请求是否符合直接放行路径，符合就直接放行
        if(checkURI){
            filterChain.doFilter(request,response);
            return;
        }
        //其他路径则判断当前登录状态，未登录跳转到登录页面
        Object employee = request.getSession().getAttribute("employee");
        if(null != employee){
            filterChain.doFilter(request,response);
            return;
        }
        //未登录状态则返回一个未登录状态结果，前端做好了页面跳转
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));

    }
    public boolean checkURI(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
