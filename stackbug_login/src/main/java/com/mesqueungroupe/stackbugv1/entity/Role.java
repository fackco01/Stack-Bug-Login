package com.mesqueungroupe.stackbugv1.entity;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
@Table(name = "role")
public class Role {
    @Id
    private Integer id;

    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;
}
