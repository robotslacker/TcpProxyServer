package com.robotslacker.tcpproxy.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class LanguageInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        String language = request.getHeader("X-Language");
        if ((language != null) && (!language.isEmpty())) {
            language = language.split(";")[0];
            for (String languageItem : language.split(","))
            {
                languageItem = languageItem.trim();
                for (Locale locale : Locale.getAvailableLocales()) {
                    if (languageItem.equalsIgnoreCase(locale.toString())) {
                        LocaleContextHolder.setLocale(new Locale(language));
                        return true;
                    }
                }
            }
            logger.info("无法识别的语言【{}】出现在来自【{}】的请求包头,已经使用默认英文。",
                    language,
                    request.getRemoteHost());
        }
        LocaleContextHolder.setLocale(Locale.US);
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
        // afterCompletion
    }
}
