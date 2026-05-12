package com.bluesoftware.city_connect_pro.entities;

import java.time.LocalDateTime;
import java.util.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "password", "roles" })
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users", indexes = {
                @Index(name = "idx_user_username", columnList = "username"),
                @Index(name = "idx_user_email", columnList = "email")
})
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Include
        private Long id;

        @NotBlank(message = "First name cannot be empty")
        @Size(max = 50)
        private String firstName;

        @NotBlank(message = "Last name cannot be empty")
        @Size(max = 50)
        private String lastName;

        @Column(nullable = false, unique = true)
        @NotBlank
        private String dni;

        @Column(nullable = false)
        @NotBlank
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Size(min = 8)
        private String password;

        @Size(max = 15)
        private String phone;

        @Size(max = 255)
        private String address;

        @Column(nullable = false, unique = true)
        @NotBlank
        @Size(min = 4, max = 20)
        private String username;

        @Column(nullable = false, unique = true)
        @NotBlank
        @Email
        private String email;

        @NotNull
        @Column(nullable = false)
        private Boolean active = true;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
        private Set<Role> roles = new HashSet<>();

        @CreationTimestamp
        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;
}