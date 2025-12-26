package com.smart_delivery_management.smartlogi_delivery.OAuth2;

import com.smart_delivery_management.smartlogi_delivery.entity.User;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.Role;
import com.smart_delivery_management.smartlogi_delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request)
            throws OAuth2AuthenticationException {

        OAuth2User oauthUser = super.loadUser(request);

        String email = oauthUser.getAttribute("email");
        String prenom = oauthUser.getAttribute("given_name");
        String nom = oauthUser.getAttribute("family_name");
        String providerId = oauthUser.getAttribute("sub");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setPrenom(prenom);
                    u.setNom(nom);
                    u.setRole(Role.CLIENT);
                    u.setProvider("GOOGLE");
                    u.setProviderId(providerId);
                    return userRepository.save(u);
                });

        return new DefaultOAuth2User(
                user.getAuthorities(),
                oauthUser.getAttributes(),
                "email"
        );
    }
}
