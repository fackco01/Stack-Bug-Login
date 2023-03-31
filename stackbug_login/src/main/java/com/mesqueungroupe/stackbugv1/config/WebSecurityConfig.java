package com.mesqueungroupe.stackbugv1.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 *@author: DuanHT
 *@since: 3/23/2023 1:24 AM
 *@description: Hoc them Web Security Config
 *@update
 *
 **/
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {



    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()

                //Authorize unrestricted when go to log in(authenticate) or register
                .authorizeHttpRequests()
                .requestMatchers("**","/auth/**", "/js/**", "/css/**","/img/**").permitAll()

                .and()
                //Restricted and only user role and admin can pass
                .authorizeHttpRequests()
                .requestMatchers("/users/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                .and()
                //Restricted and only admin role can pass
                .authorizeHttpRequests()
                .requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest()
                .authenticated()

                .and()
                .formLogin().permitAll()
                .loginPage("/auth/authenticateForm")
                .usernameParameter("email").passwordParameter("password")

                .and()
                .logout().permitAll()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
