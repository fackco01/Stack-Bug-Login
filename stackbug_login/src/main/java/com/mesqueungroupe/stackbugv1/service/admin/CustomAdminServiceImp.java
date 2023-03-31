package com.mesqueungroupe.stackbugv1.service.admin;

import com.mesqueungroupe.stackbugv1.detail.CustomUserDetail;
import com.mesqueungroupe.stackbugv1.entity.User;
import com.mesqueungroupe.stackbugv1.repository.RoleRepository;
import com.mesqueungroupe.stackbugv1.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CustomAdminServiceImp implements UserDetailsService {
    private final static String EMAIL_ADMIN_NOT_FOUND_MSG = "admin with email: %s not found";
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;

    /*=============Load Admin By email===============*/
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("service detail admin: " + email);
        User admin = userRepository.findByEmail(email).get();
        log.info("service detail admin: " + admin);
        if (admin==null){
            throw new UsernameNotFoundException(String.format(EMAIL_ADMIN_NOT_FOUND_MSG, admin));
        }
        return new CustomUserDetail(admin, roleRepository);
    }
}
