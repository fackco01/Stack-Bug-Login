package com.mesqueungroupe.stackbugv1.service.admin;

import com.mesqueungroupe.stackbugv1.detail.CustomAdminDetail;
import com.mesqueungroupe.stackbugv1.entity.Admin;
import com.mesqueungroupe.stackbugv1.repository.AdminRepository;
import com.mesqueungroupe.stackbugv1.repository.RoleRepository;
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
    private final AdminRepository adminRepository;
    @Autowired
    private final RoleRepository roleRepository;

    /*=============Load Admin By email===============*/
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("service detail admin: " + email);
        Admin admin = adminRepository.findAdminByEmail(email).get();
        log.info("service detail admin: " + admin);
        if (admin==null){
            throw new UsernameNotFoundException(String.format(EMAIL_ADMIN_NOT_FOUND_MSG, admin));
        }
        return new CustomAdminDetail(admin, roleRepository);
    }
}
