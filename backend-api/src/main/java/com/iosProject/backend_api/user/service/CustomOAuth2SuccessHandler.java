package com.iosProject.backend_api.user.service;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(CustomOAuth2SuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = authToken.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        logger.info("OAuth2 로그인 성공 - 이메일: {}", email);

        // 인증 상태를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 세션에 인증 정보 저장
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        // iOS 앱으로 직접 리다이렉션
        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
        String redirectUrl = "snapfind://oauth2/callback?email=" + encodedEmail;
        
        logger.info("앱으로 리다이렉션: {}", redirectUrl);

        // HTML 응답 생성
        String htmlResponse = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>로그인 성공</title>
                <meta http-equiv="refresh" content="0;url=%s">
            </head>
            <body>
                <script>
                    window.location.href = "%s";
                </script>
                <p>앱으로 이동 중입니다...</p>
            </body>
            </html>
            """, redirectUrl, redirectUrl);

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(htmlResponse);
    }
}
