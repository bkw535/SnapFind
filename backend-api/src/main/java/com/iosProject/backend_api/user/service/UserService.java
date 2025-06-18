package com.iosProject.backend_api.user.service;

import com.iosProject.backend_api.user.domain.User;
import com.iosProject.backend_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Map<String, String> processGoogleIdToken(String idToken, String email, String name) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            user.setName(name);
        } else {
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
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
    }
}