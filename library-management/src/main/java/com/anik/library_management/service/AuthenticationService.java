package com.anik.library_management.service;

import com.anik.library_management.dto.LoginRequestDTO;
import com.anik.library_management.dto.LoginResponseDTO;
import com.anik.library_management.dto.RegisterRequestDTO;
import com.anik.library_management.entity.User;

public interface AuthenticationService {
    User registerAdminUser(RegisterRequestDTO registerRequestDTO);
    User registerNormalUser(RegisterRequestDTO registerRequestDTO);
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
