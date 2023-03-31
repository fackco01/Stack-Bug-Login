package com.mesqueungroupe.stackbugv1.service.admin;

import com.mesqueungroupe.stackbugv1.entity.Role;
import com.mesqueungroupe.stackbugv1.entity.User;
import com.mesqueungroupe.stackbugv1.repository.RoleRepository;
import com.mesqueungroupe.stackbugv1.repository.UserRepository;
import com.mesqueungroupe.stackbugv1.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServiceImp implements AdminService {
    private final static String USER_NOT_FOUND = "User with id %s not found";
    private final static String ADMIN_NOT_FOUND = "Admin with id %s not found";
    private final static String ADMIN_EMAIL_NOT_FOUND = "Admin with email %s not found";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User userToUpdate = optionalUser.get();
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setDisplayName(user.getDisplayName());
            userToUpdate.setEmail(user.getEmail());
            return userRepository.save(userToUpdate);
        } else {
            throw new RuntimeException(String.format(USER_NOT_FOUND, id));
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new RuntimeException(String.format(USER_NOT_FOUND, id));
        }
    }

    @Override
    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createAdmin(User admin) throws Exception {
        Role adminRole = roleRepository.findById(1002)
                .orElseThrow(() -> new Exception("ROLE NOT FOUND"));
        var admin01 = User.builder()
                .email(admin.getEmail())
                .username(admin.getUsername())
                .password(encoder.encode(admin.getPassword()))
                .enable(true)
                .displayName(admin.getDisplayName())
                .createdAt(LocalDateTime.now())
                .role(adminRole)
                .build();
        return userRepository.save(admin01);
    }

    @Override
    public User updateAdmin(Long id, User admin) {
        Optional<User> optionalAdmin = userRepository.findById(id);
        if (optionalAdmin.isPresent()) {
            User adminToUpdate = optionalAdmin.get();
            adminToUpdate.setUsername(admin.getUsername());
            adminToUpdate.setDisplayName(admin.getDisplayName());
            adminToUpdate.setEmail(admin.getEmail());
            return userRepository.save(adminToUpdate);
        } else {
            throw new RuntimeException(String.format(ADMIN_NOT_FOUND, id));
        }
    }

    @Override
    public void deleteAdmin(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findAdminById(Long id) {
        Optional<User> optionalAdmin = userRepository.findById(id);
        if (optionalAdmin.isPresent()) {
            return optionalAdmin.get();
        } else {
            throw new RuntimeException(String.format(ADMIN_NOT_FOUND, id));
        }
    }

    @Override
    public User findAdminByEmail(String email) {
        Optional<User> optionalAdmin = userRepository.findByEmail(email);
        if (optionalAdmin.isPresent()) {
            return optionalAdmin.get();
        } else {
            throw new RuntimeException(String.format(ADMIN_EMAIL_NOT_FOUND, email));
        }
    }

    @Override
    public Iterable<User> findAllAdmins() {
        return userRepository.findAll();
    }

    @Override
    public User isUserExist(String email) {
        List<User> userList = userRepository.findAll();
        for (int j = 0; j < userList.size(); j++){
            if (userList.get(j).getEmail().equals(email)){
                return userList.get(j);
            }else {
               return null;
            }
        }
        return null;
    }
}
