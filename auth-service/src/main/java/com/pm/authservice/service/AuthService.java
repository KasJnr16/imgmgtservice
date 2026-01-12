package com.pm.authservice.service;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.dto.SignupRequestDTO;
import com.pm.authservice.grpc.PatientServiceGrpcClient;
import com.pm.authservice.model.User;
import com.pm.authservice.util.JwtUtil;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PatientServiceGrpcClient patientServiceGrpcClient;

    public AuthService(
            UserService userService,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            PatientServiceGrpcClient patientServiceGrpcClient) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.patientServiceGrpcClient = patientServiceGrpcClient;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        log.info("Authenticating user with email: {}", loginRequestDTO.getEmail());

        return userService.findByEmail(loginRequestDTO.getEmail())
                .filter(u -> {
                    boolean matches = passwordEncoder.matches(loginRequestDTO.getPassword(), u.getPassword());
                    log.info("Password match result for {}: {}", loginRequestDTO.getEmail(), matches);
                    return matches;
                })
                .map(u -> {
                    String token = jwtUtil.generateToken(u.getId(), u.getEmail(), u.getRole());
                    log.info("Generated JWT token for {}: {}", u.getEmail(), token);
                    return token;
                });
    }

    public Optional<User> save(User user) {
        log.info("Saving user with email: {}", user.getEmail());

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            log.warn("User with email {} already exists", user.getEmail());
            return Optional.empty();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userService.save(user);
        log.info("User saved successfully with ID: {}", savedUser.getId());
        return Optional.of(savedUser);
    }

    public Optional<String> signupAndLogin(SignupRequestDTO signupRequestDTO) {
        log.info("Signing up user with email: {}", signupRequestDTO.getEmail());

        if (userService.findByEmail(signupRequestDTO.getEmail()).isPresent()) {
            log.warn("Signup failed: User with email {} already exists", signupRequestDTO.getEmail());
            return Optional.empty();
        }

        User user = new User();
        try {
            user.setEmail(signupRequestDTO.getEmail());
            user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
            user.setRole("PATIENT");

            User newUser = userService.save(user);
            log.info("New user created with ID: {}", newUser.getId());

            patientServiceGrpcClient.createPatientAccount(
                    signupRequestDTO, String.valueOf(newUser.getId()));
            log.info("Patient account created via gRPC for user ID: {}", newUser.getId());

            String token = jwtUtil.generateToken(newUser.getId(), newUser.getEmail(), newUser.getRole());
            log.info("JWT token generated for user {}: {}", newUser.getEmail(), token);

            return Optional.of(token);

        } catch (Exception e) {
            log.error("Error during signup for {}: {}", signupRequestDTO.getEmail(), e.getMessage(), e);
            if (user.getId() != null) {
                userService.delete(user.getId());
                log.info("Rolled back user creation for ID: {}", user.getId());
            }
            return Optional.empty();
        }
    }

    public boolean validateToken(String token) {
        log.info("Validating JWT token: {}", token);

        try {
            jwtUtil.validateToken(token);
            log.info("Token is valid");
            return true;
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}
