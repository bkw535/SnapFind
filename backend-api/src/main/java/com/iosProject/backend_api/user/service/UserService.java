package com.iosProject.backend_api.user.service;

import com.iosProject.backend_api.security.JwtTokenProvider;
import com.iosProject.backend_api.user.domain.User;
import com.iosProject.backend_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public Map<String, String> processOAuthPostLogin(String email, String name, String provider, String providerId) {
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createNewUser(email, name, provider, providerId));

        String accessToken = tokenProvider.generateAccessToken(email);
        String refreshToken = tokenProvider.generateRefreshToken(email);
        
        user.updateRefreshToken(refreshToken, tokenProvider.getExpiryDateFromToken(refreshToken));
        userRepository.save(user);

        return Map.of(
            "accessToken", accessToken,
            "refreshToken", refreshToken,
            "email", email,
            "name", name
        );
    }

    @Transactional
    public Map<String, String> refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String email = tokenProvider.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new RuntimeException("Refresh token mismatch");
        }

        String newAccessToken = tokenProvider.generateAccessToken(email);
        String newRefreshToken = tokenProvider.generateRefreshToken(email);
        
        user.updateRefreshToken(newRefreshToken, tokenProvider.getExpiryDateFromToken(newRefreshToken));
        userRepository.save(user);

        return Map.of(
            "accessToken", newAccessToken,
            "refreshToken", newRefreshToken
        );
    }

    @Transactional
    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.updateRefreshToken(null, null);
        userRepository.save(user);
    }

    private User createNewUser(String email, String name, String provider, String providerId) {
        User newUser = new User(provider, providerId, email, name, LocalDateTime.now());
        return userRepository.save(newUser);
    }
} 