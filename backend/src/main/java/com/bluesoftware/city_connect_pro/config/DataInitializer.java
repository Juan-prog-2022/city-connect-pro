package com.bluesoftware.city_connect_pro.config;

import com.bluesoftware.city_connect_pro.entities.*;
import com.bluesoftware.city_connect_pro.repositories.*;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

        private final PermissionRepository permissionRepository;
        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final ProfessionalRepository professionalRepository;
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

                        Permission reviewRead = getPermission(PermissionName.REVIEW_READ);
                        Permission reviewCreate = getPermission(PermissionName.REVIEW_CREATE);
                        Permission reviewUpdate = getPermission(PermissionName.REVIEW_UPDATE);
                        Permission reviewDelete = getPermission(PermissionName.REVIEW_DELETE);

                        Permission paymentRead = getPermission(PermissionName.PAYMENT_READ);
                        Permission paymentCreate = getPermission(PermissionName.PAYMENT_CREATE);
                        Permission paymentUpdate = getPermission(PermissionName.PAYMENT_UPDATE);

                        // =====================================================
                        // CREATE ROLES
                        // =====================================================

                        Role roleAdmin = createOrUpdateRole(
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
                                                        appointmentDelete,

                                                        reviewRead,
                                                        reviewCreate,
                                                        reviewUpdate,
                                                        reviewDelete,

                                                        paymentRead,
                                                        paymentCreate,
                                                        paymentUpdate));

                        Role roleUser = createOrUpdateRole(
                                        RoleName.ROLE_USER,
                                        Set.of(
                                                        userRead,
                                                        userUpdate,
                                                        professionalRead,
                                                        appointmentRead,
                                                        appointmentCreate,
                                                        reviewRead,
                                                        reviewCreate,
                                                        paymentRead,
                                                        paymentCreate));

                        Role rolePro = createOrUpdateRole(
                                        RoleName.ROLE_PRO,
                                        Set.of(
                                                        professionalRead,
                                                        professionalCreate,
                                                        professionalUpdate,

                                                        appointmentRead,
                                                        appointmentCreate,
                                                        appointmentUpdate,

                                                        reviewRead,
                                                        paymentRead));

                        // =====================================================
                        // CREATE USERS
                        // =====================================================

                        User admin = createUserIfNotExists(
                                        "Admin",
                                        "Principal",
                                        "30111222",
                                        "admin@test.com",
                                        "admin",
                                        "3815551111",
                                        "Av. Siempre Viva 123",
                                        "admin123",
                                        Set.of(roleAdmin));

                        User user = createUserIfNotExists(
                                        "Juan",
                                        "Perez",
                                        "32111333",
                                        "juan@test.com",
                                        "juan",
                                        "3815552222",
                                        "Salta Capital",
                                        "user12345",
                                        Set.of(roleUser));

                        User professionalUser = createUserIfNotExists(
                                        "Ana",
                                        "Lopez",
                                        "34111444",
                                        "ana@test.com",
                                        "professional",
                                        "3815553333",
                                        "Tartagal, Salta",
                                        "pro12345",
                                        Set.of(rolePro));

                        // =====================================================
                        // CREATE PROFESSIONAL PROFILE
                        // =====================================================

                        createProfessionalIfNotExists(
                                        "30111222",
                                        "Ana",
                                        "Lopez",
                                        "ana@test.com",
                                        "3873511122",
                                        "Tartagal",
                                        "Av. Alberdi 455",
                                        -22.5165,
                                        -63.8013,
                                        Specialty.PSYCHOLOGIST,
                                        List.of(
                                                        "Terapia Cognitiva",
                                                        "Ansiedad",
                                                        "Depresión"),
                                        List.of(
                                                        "Universidad Nacional de Salta",
                                                        "Colegio de Psicólogos"),
                                        "Lunes a Viernes 08:00 - 18:00",
                                        new BigDecimal("15000"),
                                        CurrencyType.ARS,
                                        8,
                                        "Psicóloga especializada en terapia cognitivo conductual.",
                                        true,
                                        "https://i.pravatar.cc/300?img=12",
                                        null);

                        createProfessionalIfNotExists(
                                        "30999888",
                                        "Diego",
                                        "Herrera",
                                        "diego@test.com",
                                        "3873514455",
                                        "Tartagal",
                                        "San Martín 220",
                                        -22.5200,
                                        -63.7990,
                                        Specialty.ELECTRICIAN,
                                        List.of(
                                                        "Instalaciones",
                                                        "Cableado",
                                                        "Domótica"),
                                        List.of(
                                                        "Técnico Electricista Matriculado"),
                                        "Lunes a Sábado 09:00 - 20:00",
                                        new BigDecimal("22000"),
                                        CurrencyType.ARS,
                                        12,
                                        "Electricista con experiencia en instalaciones residenciales.",
                                        true,
                                        "https://i.pravatar.cc/300?img=15",
                                        null);

                        createProfessionalIfNotExists(
                                        "32222333",
                                        "Valentina",
                                        "Rios",
                                        "valentina@test.com",
                                        "3873519988",
                                        "Tartagal",
                                        "Belgrano 880",
                                        -22.5188,
                                        -63.8035,
                                        Specialty.NUTRITIONIST,
                                        List.of(
                                                        "Nutrición Deportiva",
                                                        "Planes Alimenticios"),
                                        List.of(
                                                        "Licenciatura en Nutrición"),
                                        "Lunes a Viernes 10:00 - 17:00",
                                        new BigDecimal("18000"),
                                        CurrencyType.ARS,
                                        5,
                                        "Nutricionista especializada en rendimiento deportivo.",
                                        true,
                                        "https://i.pravatar.cc/300?img=25",
                                        null);

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

        private Role createOrUpdateRole(
                        RoleName roleName,
                        Set<Permission> permissions) {

                return roleRepository.findByName(roleName)
                                .map(role -> {
                                        role.setPermissions(permissions);
                                        return roleRepository.save(role);
                                })
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

        private User createUserIfNotExists(
                        String firstName,
                        String lastName,
                        String dni,
                        String email,
                        String username,
                        String phone,
                        String address,
                        String rawPassword,
                        Set<Role> roles) {

                return userRepository.findByUsername(username)
                                .orElseGet(() -> {

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

                                        return userRepository.save(user);
                                });
        }

        // =====================================================
        // CREATE PROFESSIONAL
        // =====================================================

        private void createProfessionalIfNotExists(
                        String dni,
                        String firstName,
                        String lastName,
                        String email,
                        String phone,
                        String city,
                        String address,
                        Double latitude,
                        Double longitude,
                        Specialty specialty,
                        List<String> skills,
                        List<String> certifications,
                        String availability,
                        BigDecimal hourlyRate,
                        CurrencyType currency,
                        int yearsOfExperience,
                        String experienceDescription,
                        Boolean verified,
                        String profileImage,
                        String introVideo) {

                boolean exists = professionalRepository
                                .findByEmail(email)
                                .isPresent();

                if (exists) {
                        return;
                }

                Professional professional = new Professional();

                professional.setDni(dni);

                professional.setFirstName(firstName);
                professional.setLastName(lastName);

                professional.setEmail(email.toLowerCase().trim());

                professional.setPhone(phone);

                professional.setCity(city);
                professional.setAddress(address);

                professional.setLatitude(latitude);
                professional.setLongitude(longitude);

                professional.setSpecialty(specialty);

                professional.setSkills(skills);

                professional.setCertifications(certifications);

                professional.setAvailability(availability);

                professional.setHourlyRate(hourlyRate);

                professional.setCurrency(currency);

                professional.setYearsOfExperience(yearsOfExperience);

                professional.setExperienceDescription(experienceDescription);

                professional.setAverageRating(BigDecimal.ZERO);

                professional.setReviewCount(0);

                professional.setActive(true);

                professional.setVerified(verified);

                professional.setProfileImage(profileImage);

                professional.setIntroVideo(introVideo);

                professionalRepository.save(professional);
        }
}