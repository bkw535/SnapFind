package com.iosProject.backend_api.user.service;

import com.google.auth.oauth2.IdToken;
import com.iosProject.backend_api.security.JwtTokenProvider;
import com.iosProject.backend_api.user.domain.User;
import com.iosProject.backend_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Map<String, String> processGoogleIdToken(String idToken, String email, String name) {
        // (1) idToken 검증 로직이 필요하다면 추가
        // (2) 이메일로 사용자 조회
        Optional<Object> optionalUser = userRepository.findByEmail(email);
        User user;
        if (optionalUser.isPresent()) {
            // 이미 존재하면 이름만 업데이트
            user = (User) optionalUser.get();
            user.setName(name);
        } else {
            // 없으면 새로 생성
            user = User.builder()
                    .email(email)
                    .name(name)
                    .build();
        }
        userRepository.save(user);

        return Map.of(
            "result", "success",
            "email", user.getEmail(),
            "name", user.getName()
        );
    }

    public User getUserByEmail(String email) {
        return (User) userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
    }
}