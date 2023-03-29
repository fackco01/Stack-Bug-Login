package com.mesqueungroupe.stackbugv1.controller;

import com.mesqueungroupe.stackbugv1.config.JwtService;
import com.mesqueungroupe.stackbugv1.entity.Role;
import com.mesqueungroupe.stackbugv1.entity.User;
import com.mesqueungroupe.stackbugv1.repository.RoleRepository;
import com.mesqueungroupe.stackbugv1.repository.UserRepository;
import com.mesqueungroupe.stackbugv1.service.user.CustomUserServiceImp;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.EntityResponse;

import java.time.LocalDateTime;

/*
 *@author: DuanHT
 *@since: 3/21/2023 2:55 PM
 *@description:
 *@update
 *
 **/
@Controller
@AllArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AppController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    private PasswordEncoder encoder;
    private CustomUserServiceImp service;
    private AuthenticationService authService;
    private final JwtService jwtService;


//    @GetMapping("/")
//    public String viewHomePage() {
//        return "home";
//    }

    /*===========REGISTER PAGE=============*/
    @GetMapping("/registerForm")
    public String showRegistrationForm(Model model) {
        return "register_page";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute("user") User user,
                                          BindingResult biding,
                                          HttpServletResponse response) {
        if (biding.hasErrors()) {
            return "register_page";
        }
        user.setCreatedAt(LocalDateTime.now());

        if (user.getRole() == null || user.getRole().getId() == null) {

            //set default role is User Role
            Role defaultRole = roleRepository.findById(1001)
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
            user.setRole(defaultRole);
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);

        Cookie c1 = new Cookie("access", jwtService.generateToken(service.loadUserByUsername(user.getEmail())));
        c1.setMaxAge(10 * 60);
        c1.setHttpOnly(true);
        c1.setPath("/");
        response.addCookie(c1);

        Cookie c2 = new Cookie("refresh", jwtService.refreshToken(service.loadUserByUsername(user.getEmail())));
        c2.setMaxAge(30 * 60);
        c2.setHttpOnly(true);
        c2.setPath("/");
        response.addCookie(c2);
        log.info("Register success", user);

        return "redirect:/stackbug/home";
    }

    @GetMapping("/registration-success")
    public String showRegistrationSuccessPage() {
        return "registration-success";
    }

    /*===========LOGIN PAGE=============*/

    @GetMapping("/authenticateForm")
    public String authenticateForm() {
        return "login";
    }

//    @PostMapping("/authentication")
//    public String authenticate(
//            @ModelAttribute AuthenticationRequest request, HttpServletResponse response) {
//        AuthenticationResponse responseLogin = authService.authenticate(request);
//    log.info("dcm");
//        Cookie cookie = new Cookie("uid", String.valueOf(userRepository.findByEmail(request.getEmail()).get().getId()));
//        cookie.setMaxAge(10 * 60);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//
//        if (responseLogin.getRefresh() == null && responseLogin.getToken() == null) {
//            log.error("Login fail controller");
//        } else {
//            log.info("Login Success");
//            authService.saveCookie(responseLogin, response);
//        }
//        return "redirect:/stackbug/home";
//    }

    @PostMapping("/authentication")
    public String authenticate(
            @ModelAttribute AuthenticationRequest request, HttpServletResponse response) {
        AuthenticationResponse responseLogin = authService.authenticate(request);
        log.info("dcm");
        log.info(request.getEmail() + " - " + request.getPassword());
        Cookie cookie = new Cookie("uid", String.valueOf(userRepository.findByEmail(request.getEmail()).get().getId()));
        cookie.setMaxAge(10 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        if (responseLogin.getRefresh() == null && responseLogin.getToken() == null) {
            log.error("Login fail controller");
        } else {
            log.info("Login Success");
            authService.saveCookie(responseLogin, response);
        }
        return "redirect:/";
    }
}