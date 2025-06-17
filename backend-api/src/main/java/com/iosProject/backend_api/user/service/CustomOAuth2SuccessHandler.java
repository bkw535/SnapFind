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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(CustomOAuth2SuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = authToken.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        logger.info("OAuth2 로그인 성공 - 이메일: {}", email);

        // 리디렉션 URL을 중계 페이지로 변경 (이곳에서 앱에 인증 완료 알림 가능)
        String redirectUrl = "https://snapfind.p-e.kr/oauth-redirect.html?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);
        logger.info("성공 핸들러에서 리디렉션 URL로 이동: {}", redirectUrl);

        response.sendRedirect(redirectUrl);
    }
}
