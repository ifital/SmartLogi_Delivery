package com.smart_delivery_management.smartlogi_delivery.OAuth2;

import com.smart_delivery_management.smartlogi_delivery.entity.User;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.Role;
import com.smart_delivery_management.smartlogi_delivery.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {

        OAuth2User oauthUser = super.loadUser(request);
        Map<String, Object> attributes = oauthUser.getAttributes();

        String provider =
                request.getClientRegistration().getRegistrationId().toUpperCase();

        String email;
        String prenom = null;
        String nom = null;
        String providerId;
        String name;

        if ("GOOGLE".equals(provider)) {
            email = (String) attributes.get("email");
            prenom = (String) attributes.get("given_name");
            nom = (String) attributes.get("family_name");
            providerId = (String) attributes.get("sub");
            name = email;
        } else { // GITHUB
            String login = (String) attributes.get("login");
            email = attributes.get("email") != null
                    ? attributes.get("email").toString()
                    : login + "@github.com";
            providerId = attributes.get("id").toString();
            name = login;
        }

        // ✅ VARIABLES FINALES POUR LE LAMBDA
        final String finalEmail = email;
        final String finalPrenom = prenom;
        final String finalNom = nom;
        final String finalProvider = provider;
        final String finalProviderId = providerId;

        User user = userRepository.findByEmail(finalEmail)
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(finalEmail);
                    u.setPrenom(finalPrenom);
                    u.setNom(finalNom);
                    u.setRole(Role.CLIENT);
                    u.setProvider(finalProvider);
                    u.setProviderId(finalProviderId);
                    return userRepository.save(u);
                });

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                attributes,
                "name"
        );
    }
}
