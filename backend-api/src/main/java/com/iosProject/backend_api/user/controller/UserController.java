package com.iosProject.backend_api.user.controller;

import com.iosProject.backend_api.user.domain.User;
import com.iosProject.backend_api.user.service.UserHistoryService;
import com.iosProject.backend_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/oauth2/token")
    public ResponseEntity<Map<String, String>> processOAuthLogin(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");
        String email = body.get("email");
        String name = body.get("name");
        return ResponseEntity.ok(userService.processGoogleIdToken(idToken, email, name));
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    private final UserHistoryService userHistoryService;

    @GetMapping("/history")
    public List<String> getUserSearchHistory(@RequestParam Long userId) {
        return userHistoryService.getSearchHistory(userId);
    }
}
