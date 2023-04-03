package com.mesqueungroupe.stackbugv1.service.user;

import com.mesqueungroupe.stackbugv1.entity.User;
import com.mesqueungroupe.stackbugv1.repository.UserRepository;
import com.mesqueungroupe.stackbugv1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    private final static String USER_NOT_FOUND = "user %s not found";
    @Autowired
    private UserRepository userRepository;
    @Override
    public String updateUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            User userUpdate = new User();
            userRepository.save(userUpdate);
            return "Update user successfully!!!!!!";
        }
        else {
            return String.format(USER_NOT_FOUND, user);
        }
    }

    @Override
    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return "Delete data successfully!!!!!!";
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }
}
