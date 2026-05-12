package com.bluesoftware.city_connect_pro.config;

import com.bluesoftware.city_connect_pro.entities.Permission;
import com.bluesoftware.city_connect_pro.entities.PermissionName;
import com.bluesoftware.city_connect_pro.entities.Role;
import com.bluesoftware.city_connect_pro.entities.RoleName;
import com.bluesoftware.city_connect_pro.entities.User;
import com.bluesoftware.city_connect_pro.repositories.PermissionRepository;
import com.bluesoftware.city_connect_pro.repositories.RoleRepository;
import com.bluesoftware.city_connect_pro.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Set;

//@Configuration
@RequiredArgsConstructor
public class DataInitializer {

        private final PermissionRepository permissionRepository;
        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Bean
        CommandLineRunner initDatabase() {

                return args -> {

                        // =====================================================
                        // CREATE ALL PERMISSIONS
                        // =====================================================

                        Arrays.stream(PermissionName.values())
                                        .forEach(this::createPermissionIfNotExists);

                        // =====================================================
                        // LOAD PERMISSIONS
                        // =====================================================

                        Permission userRead = getPermission(PermissionName.USER_READ);
                        Permission userCreate = getPermission(PermissionName.USER_CREATE);
                        Permission userUpdate = getPermission(PermissionName.USER_UPDATE);
                        Permission userDelete = getPermission(PermissionName.USER_DELETE);

                        Permission professionalRead = getPermission(PermissionName.PROFESSIONAL_READ);
                        Permission professionalCreate = getPermission(PermissionName.PROFESSIONAL_CREATE);
                        Permission professionalUpdate = getPermission(PermissionName.PROFESSIONAL_UPDATE);
                        Permission professionalDelete = getPermission(PermissionName.PROFESSIONAL_DELETE);

                        Permission appointmentRead = getPermission(PermissionName.APPOINTMENT_READ);
                        Permission appointmentCreate = getPermission(PermissionName.APPOINTMENT_CREATE);
                        Permission appointmentUpdate = getPermission(PermissionName.APPOINTMENT_UPDATE);
                        Permission appointmentDelete = getPermission(PermissionName.APPOINTMENT_DELETE);

                        // =====================================================
                        // CREATE ROLES
                        // =====================================================

                        Role roleAdmin = createRoleIfNotExists(
                                        RoleName.ROLE_ADMIN,
                                        Set.of(
                                                        userRead,
                                                        userCreate,
                                                        userUpdate,
                                                        userDelete,

                                                        professionalRead,
                                                        professionalCreate,
                                                        professionalUpdate,
                                                        professionalDelete,

                                                        appointmentRead,
                                                        appointmentCreate,
                                                        appointmentUpdate,
                                                        appointmentDelete));

                        Role roleUser = createRoleIfNotExists(
                                        RoleName.ROLE_USER,
                                        Set.of(
                                                        userRead,
                                                        userUpdate,
                                                        appointmentRead,
                                                        appointmentCreate));

                        Role rolePro = createRoleIfNotExists(
                                        RoleName.ROLE_PRO,
                                        Set.of(
                                                        professionalRead,
                                                        professionalCreate,
                                                        professionalUpdate,

                                                        appointmentRead,
                                                        appointmentCreate,
                                                        appointmentUpdate));

                        // =====================================================
                        // CREATE USERS
                        // =====================================================

                        createUserIfNotExists(
                                        "Admin",
                                        "Principal",
                                        "30111222",
                                        "admin@test.com",
                                        "admin",
                                        "3815551111",
                                        "Av. Siempre Viva 123",
                                        "admin123",
                                        Set.of(roleAdmin));

                        createUserIfNotExists(
                                        "Usuario",
                                        "Normal",
                                        "32111333",
                                        "user@test.com",
                                        "user",
                                        "3815552222",
                                        "Salta, Salta",
                                        "user12345",
                                        Set.of(roleUser));

                        createUserIfNotExists(
                                        "Profesional",
                                        "Demo",
                                        "34111444",
                                        "pro@test.com",
                                        "professional",
                                        "3815553333",
                                        "Av. Tartagal, Salta",
                                        "pro12345",
                                        Set.of(rolePro));

                        System.out.println("✅ Initial data loaded successfully");
                };
        }

        // =====================================================
        // CREATE PERMISSION
        // =====================================================

        private void createPermissionIfNotExists(PermissionName permissionName) {

                permissionRepository.findByName(permissionName)
                                .orElseGet(() -> {

                                        Permission permission = new Permission();
                                        permission.setName(permissionName);

                                        return permissionRepository.save(permission);
                                });
        }

        // =====================================================
        // GET PERMISSION
        // =====================================================

        private Permission getPermission(PermissionName permissionName) {

                return permissionRepository.findByName(permissionName)
                                .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionName));
        }

        // =====================================================
        // CREATE ROLE
        // =====================================================

        private Role createRoleIfNotExists(
                        RoleName roleName,
                        Set<Permission> permissions) {

                return roleRepository.findByName(roleName)
                                .orElseGet(() -> {

                                        Role role = new Role();

                                        role.setName(roleName);
                                        role.setPermissions(permissions);

                                        return roleRepository.save(role);
                                });
        }

        // =====================================================
        // CREATE USER
        // =====================================================

        private void createUserIfNotExists(
                        String firstName,
                        String lastName,
                        String dni,
                        String email,
                        String username,
                        String phone,
                        String address,
                        String rawPassword,
                        Set<Role> roles) {

                if (userRepository.existsByUsername(username)) {
                        return;
                }

                User user = new User();

                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setDni(dni);

                user.setEmail(email.toLowerCase().trim());
                user.setUsername(username.toLowerCase().trim());
                user.setPhone(phone);
                user.setAddress(address);

                user.setPassword(
                                passwordEncoder.encode(rawPassword));

                user.setActive(true);

                user.setRoles(roles);

                userRepository.save(user);
        }
}
