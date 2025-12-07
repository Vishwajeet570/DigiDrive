// package com.digidrive.document_service.service;

// import com.digidrive.document_service.dto.*;
// import com.digidrive.document_service.entity.User;
// import com.digidrive.document_service.repository.UserRepository;
// import com.digidrive.document_service.security.JwtService;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// @Service
// public class AuthService {

//     private final UserRepository repo;
//     private final PasswordEncoder encoder;
//     private final JwtService jwtService;

//     public AuthService(UserRepository repo, PasswordEncoder encoder, JwtService jwtService) {
//         this.repo = repo;
//         this.encoder = encoder;
//         this.jwtService = jwtService;
//     }

//     public AuthResponse register(RegisterRequest request) {
//         User user = User.builder()
//                 .email(request.email())
//                 .password(encoder.encode(request.password()))
//                 .build();

//         repo.save(user);
//         return new AuthResponse(jwtService.generateToken(user.getEmail()));
//     }

//     public AuthResponse login(LoginRequest request) {
//         User user = repo.findByEmail(request.email())
//                 .orElseThrow(() -> new RuntimeException("User not found"));

//         if (!encoder.matches(request.password(), user.getPassword())) {
//             throw new RuntimeException("Invalid password");
//         }

//         return new AuthResponse(jwtService.generateToken(user.getEmail()));
//     }
// }

package com.digidrive.document_service.service;

import com.digidrive.document_service.dto.*;
import com.digidrive.document_service.entity.Role;
import com.digidrive.document_service.entity.User;
import com.digidrive.document_service.repository.UserRepository;
import com.digidrive.document_service.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UserRepository repo, PasswordEncoder encoder, JwtService jwtService) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        Role role;
        try {
            role = Role.valueOf(request.role() == null ? "ROLE_USER" : request.role());
        } catch (Exception e) {
            role = Role.ROLE_USER;
        }

        User user = User.builder()
                .email(request.email())
                .password(encoder.encode(request.password()))
                .role(role)
                .build();

        repo.save(user);

        String token = jwtService.generateToken(user.getEmail(), List.of(user.getRole().name()));
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = repo.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail(), List.of(user.getRole().name()));
        return new AuthResponse(token);
    }
}
