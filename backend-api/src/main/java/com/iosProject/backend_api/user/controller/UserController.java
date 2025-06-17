package com.iosProject.backend_api.user.controller;

import com.iosProject.backend_api.user.domain.User;
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
    public ResponseEntity<Map<String, String>> processOAuthLogin(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");
        return ResponseEntity.ok(userService.processGoogleIdToken(idToken));
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
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
}
