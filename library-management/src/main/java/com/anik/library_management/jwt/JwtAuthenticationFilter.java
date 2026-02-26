package com.anik.library_management.jwt;

import com.anik.library_management.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    @Autowired
    private final JwtService  jwtService;
//    @Autowired
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        // Check if Authorization header is not present Or not starts with 'Bearer'
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT token from header
        jwtToken = authHeader.substring(7);
        username = jwtService.extractUsername(jwtToken);

        // Cheek if we have a username and no authentication exit yet
        if(username != null && SecurityContextHolder
                .getContext().getAuthentication() == null) {
            // Get the User details from database
            var userDetails = userRepository.findByUsername(username)
                    .orElseThrow(()-> new RuntimeException("User not found!"));

            // Validate the token
            if(jwtService.isTokenValid(jwtToken, userDetails)){
                // Create the authentication with user roles
                List<SimpleGrantedAuthority> authorities = userDetails.getRoles()
                        .stream().map(role -> new SimpleGrantedAuthority(role))
                        .toList();

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                // Set authentication details
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Update Security Context with authentication
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }
        filterChain.doFilter(request, response);
    }
}
