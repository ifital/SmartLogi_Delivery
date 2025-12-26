package com.smart_delivery_management.smartlogi_delivery.controller;

import com.smart_delivery_management.smartlogi_delivery.dto.LoginRequest;
import com.smart_delivery_management.smartlogi_delivery.dto.LoginResponse;
import com.smart_delivery_management.smartlogi_delivery.dto.RegisterRequest;
import com.smart_delivery_management.smartlogi_delivery.dto.RegisterResponse;
import com.smart_delivery_management.smartlogi_delivery.security.JwtUtil;
import com.smart_delivery_management.smartlogi_delivery.security.UserDetailsServiceImpl;
import com.smart_delivery_management.smartlogi_delivery.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserService userService;

    // ====================== LOGIN CLASSIQUE ======================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            // 🔐 Authentification email / password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // 👤 Charger l'utilisateur
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(request.getEmail());

            // 🎟️ Génération du JWT
            String token = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new LoginResponse(token));

        } catch (BadCredentialsException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
    }

    // ====================== REGISTER ======================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        userService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterResponse("User registered successfully"));
    }
}
