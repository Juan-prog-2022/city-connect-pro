package com.bluesoftware.city_connect_pro.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@ToString(exclude = "roles")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, length = 50)
    private PermissionName name;

    @JsonIgnoreProperties({ "permissions", "hibernateLazyInitializer", "handler" })
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();
}