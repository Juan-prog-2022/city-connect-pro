package com.bluesoftware.city_connect_pro.controllers;

import com.bluesoftware.city_connect_pro.dtos.*;
import com.bluesoftware.city_connect_pro.mapper.*;
import com.bluesoftware.city_connect_pro.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public List<UserResponseDTO> getAll() {
        return userService.getAllUsers()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public UserResponseDTO getById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
