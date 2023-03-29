package com.mesqueungroupe.stackbugv1.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
*@author: DuanHT
*@since: 3/29/2023 10:40 AM
*@description: 
*@update
*
**/
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private final JwtService service;
    @Autowired
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String authHeader = null;
        String reAuthHeader = null;

        if (cookies != null) {
            for (Cookie cookie:cookies){
                if (cookie.getName().equals("access")){
                    authHeader = cookie.getValue();
                    log.info("Get token access from cookie {}", authHeader);
                }
            }
        }

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    reAuthHeader = cookie.getValue();
                    log.info("Get token refresh access from cookie {}", reAuthHeader);
                }
            }
        }

        final String jwt;
        final String userMail;
        //Check token access has exits
        if (authHeader == null && reAuthHeader == null){
            log.info("Filter pass when not Authorization");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (authHeader == null){
                jwt = reAuthHeader;
            }else {
                jwt = authHeader;
            }
            userMail = service.extractUsername(jwt);
            //Check user has exits and has login yet
            if (userMail != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails user = this.userDetailsService.loadUserByUsername(userMail);
                log.info("Filter pass with email: {} and role(s): {} ", user.getUsername(), user.getAuthorities());
                //Check token is valid
                if (service.isTokenValid(jwt, user)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null, //Don't know credentials
                            user.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response);
                }
            }
        } catch (Exception ex) {
            log.error("Something wrong is happen {}", ex.getMessage());
            response.setHeader("ERROR", ex.getMessage());
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("ERROR_MSG", ex.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }
}
