package com.iosProject.backend_api.user.controller;

import com.iosProject.backend_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/oauth2/token")
    public ResponseEntity<Map<String, String>> processOAuthLogin(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String provider,
            @RequestParam String providerId) {
        return ResponseEntity.ok(userService.processOAuthPostLogin(email, name, provider, providerId));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok(userService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String email) {
        userService.logout(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getCurrentUser(@RequestParam String email) {
        return ResponseEntity.ok(Map.of("email", email));
    }
}
