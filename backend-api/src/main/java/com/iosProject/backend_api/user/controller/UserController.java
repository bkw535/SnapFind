package com.iosProject.backend_api.user.controller;

import ch.qos.logback.classic.Logger;
import com.iosProject.backend_api.user.domain.User;
import com.iosProject.backend_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;

    @GetMapping("/oauth2/callback-to-app")
    public String redirectToApp(@RequestParam(required = false) String email) {
        logger.info("OAuth2 콜백 도착 - email: {}", email);
        return "redirect:/oauth-redirect.html";
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal org.springframework.security.oauth2.core.user.OAuth2User principal) {
        String provider = "google";
        String providerId = principal.getAttribute("sub"); // Google의 고유 사용자 ID

        // DB에서 유저 찾기
        User user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 간단한 DTO로 반환
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "name", user.getName()
        ));
    }
}
