package com.pm.authservice.service;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.dto.SignupRequestDTO;
import com.pm.authservice.dto.SignupResponseDTO;
import com.pm.authservice.grpc.MedicalStaffGrpcClient;
import com.pm.authservice.grpc.PatientServiceGrpcClient;
import com.pm.authservice.model.User;
import com.pm.authservice.util.JwtUtil;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final PatientServiceGrpcClient patientServiceGrpcClient;
  private final MedicalStaffGrpcClient medicalStaffGrpcClient;

  public AuthService(
      UserService userService,
      PasswordEncoder passwordEncoder,
      JwtUtil jwtUtil,
      PatientServiceGrpcClient patientServiceGrpcClient,
      MedicalStaffGrpcClient medicalStaffGrpcClient) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
    this.patientServiceGrpcClient = patientServiceGrpcClient;
    this.medicalStaffGrpcClient = medicalStaffGrpcClient;
  }

  public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
    return userService.findByEmail(loginRequestDTO.getEmail())
        .filter(u -> passwordEncoder.matches(
            loginRequestDTO.getPassword(),
            u.getPassword()))
        .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole()));
  }

public Optional<SignupResponseDTO> signupStaff(SignupRequestDTO signupRequestDTO) {
    // 1. Check if user already exists
    if (userService.findByEmail(signupRequestDTO.getEmail()).isPresent()) {
        return Optional.empty();
    }

    // 2. Create new user
    User user = new User();
    try {
        user.setEmail(signupRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        user.setRole(signupRequestDTO.getRole()); // STAFF role or from DTO

        User newUser = userService.save(user);

        // 3. Call medical-staff-service via gRPC
        var response = medicalStaffGrpcClient.createMedicalStaff(signupRequestDTO, newUser.getId().toString());

        // 4. Return SignupResponseDTO
        SignupResponseDTO responseDTO = new SignupResponseDTO(
            newUser.getId().toString(),
            response.getName(),
            newUser.getEmail()
        );

        return Optional.of(responseDTO);

    } catch (Exception e) {
        // Rollback user creation if anything fails
        if (user.getId() != null) {
            userService.delete(user.getId());
        }
        return Optional.empty();
    }
}


  public Optional<String> signupAndLogin(SignupRequestDTO signupRequestDTO) {

    // 1. Check if user already exists
    if (userService.findByEmail(signupRequestDTO.getEmail()).isPresent()) {
      return Optional.empty();
    }

    // 2. Create new user
    User user = new User();
    try {

      user.setEmail(signupRequestDTO.getEmail());
      user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
      user.setRole("PATIENT");

      User newUser = userService.save(user);

      patientServiceGrpcClient.createPatientAccount(
          signupRequestDTO, String.valueOf(newUser.getId()));

      // 3. Auto-login after signup (generate token)
      String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
      return Optional.of(token);
      
    } catch (Exception e) {
      if (user.getId() != null) {
      userService.delete(user.getId());
      }
      return Optional.empty();
    }
  }

  public boolean validateToken(String token) {
    try {
      jwtUtil.validateToken(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
