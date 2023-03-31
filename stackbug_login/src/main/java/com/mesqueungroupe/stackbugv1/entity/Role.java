package com.mesqueungroupe.stackbugv1.entity;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    private Integer id;

    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;
}
