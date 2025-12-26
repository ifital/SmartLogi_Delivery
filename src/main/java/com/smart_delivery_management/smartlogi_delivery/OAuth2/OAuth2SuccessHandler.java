package com.smart_delivery_management.smartlogi_delivery.OAuth2;

import com.smart_delivery_management.smartlogi_delivery.entity.User;
import com.smart_delivery_management.smartlogi_delivery.repository.UserRepository;
import com.smart_delivery_management.smartlogi_delivery.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        User user = userRepository.findByEmail(email).orElseThrow();

        String jwt = jwtUtil.generateToken((UserDetails) user);

        response.setContentType("application/json");
        response.getWriter().write("""
            {
              "token": "%s"
            }
        """.formatted(jwt));
    }
}
