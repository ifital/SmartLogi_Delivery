package com.smart_delivery_management.smartlogi_delivery.OAuth2;

import com.smart_delivery_management.smartlogi_delivery.entity.User;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.Role;
import com.smart_delivery_management.smartlogi_delivery.repository.UserRepository;
import com.smart_delivery_management.smartlogi_delivery.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String email = oauth2User.getAttribute("email");

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email not provided by OAuth2 provider");
            return;
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = new User();
            user.setEmail(email);
            user.setRole(Role.CLIENT); // ✅ ENUM
            userRepository.save(user);
        }

        String token = jwtUtil.generateTokenFromEmail(user.getEmail());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("""
            {
              "token": "%s"
            }
        """.formatted(token));
    }
}
