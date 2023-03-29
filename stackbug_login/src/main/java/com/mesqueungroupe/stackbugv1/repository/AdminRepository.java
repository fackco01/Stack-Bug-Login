package com.mesqueungroupe.stackbugv1.repository;

import com.mesqueungroupe.stackbugv1.entity.Admin;
import com.mesqueungroupe.stackbugv1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<Admin> findAdminByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<Admin> findAdminByUsername(String username);
}
