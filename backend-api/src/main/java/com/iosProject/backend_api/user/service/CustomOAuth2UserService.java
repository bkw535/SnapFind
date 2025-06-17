package com.iosProject.backend_api.user.service;

import com.iosProject.backend_api.user.domain.User;
import com.iosProject.backend_api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        try {
            OAuth2User oAuth2User = super.loadUser(request);

            String provider = request.getClientRegistration().getRegistrationId();
            String providerId = oAuth2User.getAttribute("sub");
            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");

            if (email == null || providerId == null) {
                OAuth2Error oauth2Error = new OAuth2Error("missing_required_attribute", 
                    "Required attributes are missing", null);
                throw new OAuth2AuthenticationException(oauth2Error);
            }

            User user = userRepository.findByProviderAndProviderId(provider, providerId)
                    .orElseGet(() -> {
                        User newUser = User.builder()
                                .provider(provider)
                                .providerId(providerId)
                                .email(email)
                                .name(name)
                                .createdAt(LocalDateTime.now())
                                .build();
                        return userRepository.save(newUser);
                    });

            return oAuth2User;
        } catch (OAuth2AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            OAuth2Error oauth2Error = new OAuth2Error("user_load_error", 
                "Failed to load user: " + e.getMessage(), null);
            throw new OAuth2AuthenticationException(oauth2Error);
        }
    }
}
