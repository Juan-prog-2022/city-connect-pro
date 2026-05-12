package com.bluesoftware.city_connect_pro.repositories;

import com.bluesoftware.city_connect_pro.entities.Permission;
import com.bluesoftware.city_connect_pro.entities.PermissionName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByName(PermissionName name);
}