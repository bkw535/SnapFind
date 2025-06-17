package com.iosProject.backend_api.user.controller;

import com.iosProject.backend_api.user.domain.User;
import com.iosProject.backend_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/oauth2/callback-to-app")
    public String redirectToApp() {
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
