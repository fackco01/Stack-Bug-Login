package com.mesqueungroupe.stackbugv1.service;

import com.mesqueungroupe.stackbugv1.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public String updateUser(Long id);
    public String deleteUser(Long id);
    public User findUserByEmail(String email);
}
