package com.mesqueungroupe.stackbugv1.service;

import com.mesqueungroupe.stackbugv1.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {
    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    User findUserById(Long id);

    Iterable<User> findAllUsers();

    User createAdmin(User admin) throws Exception;

    User updateAdmin(Long id, User admin);

    void deleteAdmin(Long id);

    User findAdminById(Long id);

    User findAdminByEmail(String email);

    Iterable<User> findAllAdmins();
    User isUserExist(String email);

}
