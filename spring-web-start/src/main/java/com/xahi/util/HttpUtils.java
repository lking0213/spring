package com.xahi.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public final class HttpUtils {

    public static void checkLocalAccess(final HttpServletRequest request) {
        final String ip = HttpUtils.getClientIP(request);
        if ("127.0.0.1".equals(ip)) {
            return;
        }

        String localIP = "....";
        try {
            localIP = InetAddress.getLocalHost().getHostAddress();
        } catch (final UnknownHostException unknownhostexception) {
            log.error("System error.", unknownhostexception);
        }
        if (localIP.equals(ip)) {
            return;
        }

        final String msg = "Invalid IP: " + ip;
        log.warn(msg + ", " + request.getRequestURL());
        throw new IllegalStateException(msg);
    }

    public static String getClientIP(final HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String fromSource = "X-Real-IP";
        String ip = request.getHeader("X-Real-IP");
        if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            fromSource = "X-Forwarded-For";
            if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                fromSource = "Proxy-Client-IP";
                if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                    fromSource = "WL-Proxy-Client-IP";
                    if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
                        ip = request.getHeader("HTTP_CLIENT_IP");
                        fromSource = "HTTP_CLIENT_IP";
                        if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
                            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                            fromSource = "HTTP_X_FORWARDED_FOR";
                            if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
                                ip = request.getRemoteAddr();
                                fromSource = "request.getRemoteAddr";
                            }
                        }
                    }
                }
            }
        }

        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (final UnknownHostException unknownhostexception) {
                log.error("System error.", unknownhostexception);
            }
        }
        if (ip.indexOf(",") > 0) {
            final String[] ips = ip.split(",");
            for (String temp : ips) {
                temp = temp.trim();
                if (!temp.contains("unknown")) {
                    ip = temp;
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Parsed IP[{}], from: {}", ip, fromSource);
        }
        return ip;
    }
}