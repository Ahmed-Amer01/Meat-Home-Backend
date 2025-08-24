package com.example.meat_home.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.meat_home.repository.CustomerRepository;
import com.example.meat_home.repository.StaffRepository;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomerRepository customerRepo;
    private final StaffRepository staffRepo;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   CustomerRepository customerRepo,
                                   StaffRepository staffRepo) {
        this.jwtService = jwtService;
        this.customerRepo = customerRepo;
        this.staffRepo = staffRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Allow CORS preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                String email = jwtService.extractEmail(token);
                String role = jwtService.extractRole(token);
                Integer tokenVersion = jwtService.extractVersion(token);

                // Check token version against DB
                boolean valid = false;
                if ("CUSTOMER".equalsIgnoreCase(role)) {
                    var customer = customerRepo.findByEmail(email).orElse(null);
                    if (customer != null && customer.getTokenVersion().equals(tokenVersion)) {
                        valid = true;
                    }
                } else {
                    var staff = staffRepo.findByEmail(email).orElse(null);
                    if (staff != null && staff.getTokenVersion().equals(tokenVersion)) {
                        valid = true;
                    }
                }

                if (!valid) {
                    throw new RuntimeException("Token expired due to logout or reset");
                }

                // convert role to uppercase to  prevent case sensitivity issues
                List<SimpleGrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

                // Create Authentication object
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                // Save Authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (Exception e) {
                // Send JSON response for invalid token
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"message\":\"Invalid or expired token\", \"status\":401}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}