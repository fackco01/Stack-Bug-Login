package com.mesqueungroupe.stackbugv1.controller;

import com.mesqueungroupe.stackbugv1.entity.User;
import com.mesqueungroupe.stackbugv1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/")
    public ResponseEntity<String> viewUserPage(){
        return new ResponseEntity<>("Hello User", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id) {
        return userService.updateUser(id);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/find")
    public ResponseEntity<User> findUserByEmail(@RequestParam("email") String email){
        User user = userService.findUserByEmail(email);
        if (user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
