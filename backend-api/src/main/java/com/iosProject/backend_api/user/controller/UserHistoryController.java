package com.iosProject.backend_api.user.controller;

import com.iosProject.backend_api.user.service.UserHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserHistoryController {

    private final UserHistoryService userHistoryService;

    @GetMapping("/history")
    public List<String> getUserSearchHistory(@RequestParam Long userId) {
        return userHistoryService.getSearchHistory(userId);
    }
}
