package com.mesqueungroupe.stackbugv1.repository;

import com.mesqueungroupe.stackbugv1.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
