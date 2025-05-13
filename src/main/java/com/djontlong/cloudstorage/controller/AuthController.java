package com.djontlong.cloudstorage.controller;

import com.djontlong.cloudstorage.dto.AuthRequest;
import com.djontlong.cloudstorage.model.User;
import com.djontlong.cloudstorage.repository.UserRepository;
import com.djontlong.cloudstorage.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest authRequest) {
        // Проверяем, существует ли пользователь
        if (userRepository.findByUsername(authRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Создаём нового пользователя
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        // Генерируем токен
        String token = jwtUtil.generateToken(authRequest.getUsername());
        return ResponseEntity.ok(token);
    }
}