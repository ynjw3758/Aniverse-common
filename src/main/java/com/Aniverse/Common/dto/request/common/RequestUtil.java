package com.Aniverse.Common.dto.request.common;

import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class RequestUtil {
	
    public static String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank() && !"unknown".equalsIgnoreCase(realIp)) {
            return realIp;
        }

        return request.getRemoteAddr();
    }

}
