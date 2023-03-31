package com.mesqueungroupe.stackbugv1.config;

import com.mesqueungroupe.stackbugv1.entity.Role;
import com.mesqueungroupe.stackbugv1.entity.User;
import com.mesqueungroupe.stackbugv1.repository.RoleRepository;
import com.mesqueungroupe.stackbugv1.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class DatabaseLoader{
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    @Bean
    public void init_role() throws Exception{
        var uRole = Role.builder()
                .id(1001)
                .name("ROLE_USER")
                .build();
        var adRole = Role.builder()
                .id(1002)
                .name("ROLE_ADMIN")
                .build();
        List<Role> list = new ArrayList<>();
        list.add(uRole);
        list.add(adRole);
        roleRepository.saveAll(list);
    }
    @Bean
    public void init_admin() throws Exception {

        Role adminRole = roleRepository.findById(1002)
                .orElseThrow(() -> new Exception("ROLE NOT FOUND"));
        var user = User.builder()
                .email("admin@admin.com")
                .username("admin01")
                .password(encoder.encode("1234"))
                .enable(true)
                .displayName("Admin 01")
                .createdAt(LocalDateTime.now())
                .role(adminRole)
                .build();
        List<User> userList = userRepository.findAll();
        for (int j = 0; j < userList.size(); j++){
            if (userList.get(j).getEmail().equals(user.getEmail())){
                return;
            }else {
                userRepository.save(user);
            }
        }
    }
}
