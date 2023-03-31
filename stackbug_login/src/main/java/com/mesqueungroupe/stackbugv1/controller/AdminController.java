package com.mesqueungroupe.stackbugv1.controller;

import com.mesqueungroupe.stackbugv1.entity.User;
import com.mesqueungroupe.stackbugv1.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    @Autowired
    private final AdminService adminService;

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers(){
        Iterable<User> users = adminService.findAllUsers();
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @PostMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = adminService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = adminService.updateUser(id, user);
        return new ResponseEntity(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User admin = adminService.findAdminByEmail(email);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/checkEmail")
    public ResponseEntity<User> isEmailExist(HttpServletRequest request) {
        String email = request.getParameter("email");
//        log.info("Check email from ajax: {}", email);
        User admin = adminService.isUserExist(email);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ModelAndView viewAdminPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin_page");
        return mav;
    }
}
