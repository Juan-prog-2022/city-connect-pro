package com.bluesoftware.city_connect_pro.services;

import java.util.List;
import java.util.Optional;

import com.bluesoftware.city_connect_pro.entities.User;

public interface IUserService {

    // 🔍 Get all
    List<User> getAllUsers();

    // 🔍 Get by ID
    Optional<User> getUserById(Long id);

    // 💾 Create
    User createUser(User user);

    // 🔄 Update
    User updateUser(Long id, User userDetails);

    // ❌ Delete
    void deleteUser(Long id);

    // 🔍 Finders
    Optional<User> getByEmail(String email);
    Optional<User> getByDni(String dni);
    Optional<User> getByUsername(String username);

    // 🔥 Login
    Optional<User> findByUsernameOrEmail(String value);

    // 🔒 Validations
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
}