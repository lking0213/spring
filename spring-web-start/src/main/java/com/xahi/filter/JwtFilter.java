package com.xahi.filter;

import com.xahi.util.Constants;
import com.xahi.util.HttpUtils;
import com.xahi.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by liwq on 2019/7/18 16:29
 */
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter{

    @Autowired
    @Qualifier("userDetailServiceImpl")
    private UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (request.getRequestURI().startsWith("/pub/")) {
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("in JwtFilter ...");
        String clientIp = HttpUtils.getClientIP(request);
        String token = request.getHeader(Constants.AUTHORIZATION);

        if (StringUtils.isEmpty(token)) {
            log.warn("[{}]JWT empty token", clientIp);
            return;
        }

        try {
            Map<String, Object> claims = JwtUtil.getClaims(token);
            if (claims.isEmpty()){
                log.warn("[{}]JWT auth fail", clientIp);
            }

            Object username = claims.get("username");
            UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(username));
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                    null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            token = JwtUtil.refreshToken(token);
            response.addHeader(Constants.AUTHORIZATION, token);

            filterChain.doFilter(request, response);
        } catch (Exception e){
            e.printStackTrace();
            log.warn("[{}]JWT auth fail: {}", clientIp, e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

    }
}
