package com.mesqueungroupe.stackbugv1.service.user;

import com.mesqueungroupe.stackbugv1.detail.CustomUserDetail;
import com.mesqueungroupe.stackbugv1.entity.User;
import com.mesqueungroupe.stackbugv1.repository.RoleRepository;
import com.mesqueungroupe.stackbugv1.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomUserServiceImp implements UserDetailsService {
    private final static String EMAIL_NOT_FOUND_MSG = "user with email: %s not found";
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;

    /*=============Load User By Username===============*/
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("service detail user: " +  email);
        User user = userRepository.findByEmail(email).get();
        log.info("service detail: " +  user);
        if (user==null){
            throw new UsernameNotFoundException(String.format(EMAIL_NOT_FOUND_MSG, email));
        }
        return new CustomUserDetail(user,roleRepository);
    }

}
