package com.mesqueungroupe.stackbugv1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mesqueungroupe.stackbugv1.config.JwtService;
import com.mesqueungroupe.stackbugv1.entity.Role;
import com.mesqueungroupe.stackbugv1.entity.User;
import com.mesqueungroupe.stackbugv1.repository.RoleRepository;
import com.mesqueungroupe.stackbugv1.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtService service;
    private final AuthenticationManager manager;
    private final UserDetailsService userDetailsService;

    //Check user info to login
    public AuthenticationResponse authenticate(@NotNull AuthenticationRequest request){
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("USER NAME NOT FOUND")
        );
        log.info("Check same password {}", encoder.matches(request.getPassword(),user.getPassword()));
        if (user == null || !encoder.matches(request.getPassword(),user.getPassword())){
            log.info("Login Fail service!!!");
            return new AuthenticationResponse();
        }else {
             var jwtToken = service.generateToken(user);
             var jwtRefresh = service.refreshToken(user);
            return new AuthenticationResponse(jwtToken, jwtRefresh);
        }
    }

    //Is User Exits
    public User isUserExists(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElse(null);
    }

    //Save cookie
    public void saveCookie(AuthenticationResponse authenticationResponse,
                           HttpServletResponse httpServletResponse){
        log.info("save {} to HttpOnly Cookie: ", authenticationResponse);
        Cookie jwtAccess = new Cookie("access", authenticationResponse.getToken());
        jwtAccess.setMaxAge(10 * 60);
        jwtAccess.setHttpOnly(true);
        jwtAccess.setPath("/");

        httpServletResponse.addCookie(jwtAccess);

        Cookie jwtRefresh = new Cookie("refresh", authenticationResponse.getRefresh());
        jwtRefresh.setMaxAge(30 * 60);
        jwtRefresh.setHttpOnly(true);
        jwtRefresh.setPath("/");

        httpServletResponse.addCookie(jwtRefresh);
    }

    //Refresh cookie
    public AuthenticationResponse refreshCookie(HttpServletRequest request,
                                                HttpServletResponse response) throws IOException {
        String jwt;
        String userEmail;
        String authHeader = null;

        log.info("Refresh cookie to make new access token and refresh token");

        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("refresh")){
                    authHeader = cookie.getValue();
                }
            }
        }
        try {
            jwt = authHeader;
            userEmail = service.extractUsername(jwt);
            log.info("refresh token get: {}", userEmail);
            if (!userEmail.isEmpty()) {
                UserDetails user = userDetailsService.loadUserByUsername(userEmail);
                log.info("refresh user: {} ", user);
                log.debug("check valid token refresh");
                if (service.isTokenValid(jwt, user)) {
                    log.info("Access {} and refresh {}", service.generateToken(user), service.refreshToken(user));
                    return new AuthenticationResponse(service.generateToken(user), service.refreshToken(user));
                }
            }
        } catch (Exception e) {
            log.error("Something wrong happen");
            response.setHeader("ERROR", e.getMessage());
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("ERROR_MSG", e.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
        return new AuthenticationResponse();
    }

    //Register
    public AuthenticationResponse register(RegisterRequest request) throws Exception {
        Optional<Role> defaultRoleOptional = roleRepository.findByName("ROLE_USER"); // thay "ROLE_USER" bằng tên role mặc định của bạn
        if (!defaultRoleOptional.isPresent()) {
            throw new Exception("Default role not found.");
        }
        Role defaultRole = defaultRoleOptional.get();
        var user = User.builder()
                .displayName(request.getDisplayName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .username(request.getUsername())
                .roles(new ArrayList<>())
                .defaultRole(defaultRole)
                .createdAt(LocalDateTime.now())
                .enable(true)
                .build();
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                log.error("Exist email {} in DB", user.getEmail());
                log.info("info {}", userRepository.findByEmail(user.getEmail()));
                return null;
            } else {
                Collection<String> roleStr = request.getRoles();
                List<Role> role = new ArrayList<>();
                roleStr.forEach(str -> {
                    Role r = roleRepository.findByName(str).orElse(null);
                    if (r != null) {
                        role.add(r);
                    }
                });
                log.info("List role register: {}", role);
                role.forEach(r -> user.getRoles().add(r));
                userRepository.save(user);
                var jwtToken = service.generateToken(user);
                var jwtRefresh = service.refreshToken(user);

                log.info("Get getAuthorities: {}", user.getAuthorities());
                log.info("Register Token access: {}", jwtToken);
                log.info("Register Token refresh: {}", jwtRefresh);
                return new AuthenticationResponse(jwtToken, jwtRefresh);
            }
        } catch (Exception e) {
            log.error("Something wrong happen: {}", e.getMessage());
            userRepository.delete(user);
        }
        return null;
    }
}
