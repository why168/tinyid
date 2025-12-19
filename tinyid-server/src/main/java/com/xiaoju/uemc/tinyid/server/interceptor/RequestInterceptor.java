package com.xiaoju.uemc.tinyid.server.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * @Author du_imba
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    private static final String START_TIME_ATTR = "requestStartTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(START_TIME_ATTR, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String params = "";
        Map<String, String[]> paramsMap = request.getParameterMap();
        if (paramsMap != null && !paramsMap.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String[]> entry : paramsMap.entrySet()) {
                sb.append(entry.getKey()).append(":").append(StringUtils.arrayToDelimitedString(entry.getValue(), ",")).append(";");
            }
            params = sb.toString();
        }

        Long startTime = (Long) request.getAttribute(START_TIME_ATTR);
        long cost = (startTime != null) ? System.currentTimeMillis() - startTime : 0;
        logger.info("request interceptor path={}, cost={}, params={}", request.getServletPath(), cost, params);
    }
}
