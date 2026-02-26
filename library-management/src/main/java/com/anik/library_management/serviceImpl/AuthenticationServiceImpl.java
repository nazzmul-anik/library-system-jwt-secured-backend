package com.anik.library_management.serviceImpl;

import com.anik.library_management.dto.LoginRequestDTO;
import com.anik.library_management.dto.LoginResponseDTO;
import com.anik.library_management.dto.RegisterRequestDTO;
import com.anik.library_management.entity.User;
import com.anik.library_management.jwt.JwtService;
import com.anik.library_management.repository.UserRepository;
import com.anik.library_management.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    public AuthenticationServiceImpl(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager  authenticationManager;

    @Override
    public User registerAdminUser(RegisterRequestDTO registerRequestDTO) {
        if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
            throw new RuntimeException("User already registered.");
        }

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public User registerNormalUser(RegisterRequestDTO registerRequestDTO) {
        if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
            throw new RuntimeException("User already registered.");
        }

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()));

        User user = userRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found."));
        String token = jwtService.generateToken(user);
        return LoginResponseDTO.builder()
                .token(token)
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();
    }
}
