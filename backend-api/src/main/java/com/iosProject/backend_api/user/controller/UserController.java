package com.iosProject.backend_api.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/oauth2/callback-to-app")
    public String redirectToApp() {
        return "redirect:/oauth-redirect.html";  // static 폴더의 HTML 반환
    }
}
