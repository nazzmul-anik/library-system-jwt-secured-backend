package com.anik.library_management.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class LoginResponseDTO {
    private String token;
    private String username;
    private Set<String> roles;
}
