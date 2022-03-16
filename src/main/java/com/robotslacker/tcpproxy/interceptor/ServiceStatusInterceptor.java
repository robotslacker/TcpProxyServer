package com.robotslacker.tcpproxy.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.robotslacker.tcpproxy.service.TcpProxyBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServiceStatusInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        if (!"RUNNING".equals(TcpProxyBaseService.serviceStatus)) {
            try {
                JSONObject ret = new JSONObject();
                ret.put("errorCode", "PROXY-S00006");
                switch (TcpProxyBaseService.serviceStatus)
                {
                    case "STARTING":
                        ret.put("errMsg", "服务正在初始化，请稍后再尝试.");
                        break;
                    case "SHUTTING":
                        ret.put("errMsg", "服务正在关闭中, 无法继续使用.");
                        break;
                    default:
                        ret.put("errMsg", "系统不提供服务【" + TcpProxyBaseService.serviceStatus + "】");
                        break;
                }
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Cache-Control", "no-cache");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.getWriter().println(ret.toJSONString());
                response.getWriter().flush();
                logger.info("系统当前正在初始化，来自【{}】的请求已经被拒绝.", request.getRemoteHost());
            } catch (IOException ie) {
                logger.error("拦截器错误.", ie);
            }
            return false;
        }
        // 如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        // 如果设置为true时，请求将会继续执行后面的操作
        return true;

    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) {
        // postHandle
    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        // postHandle
    }
}
