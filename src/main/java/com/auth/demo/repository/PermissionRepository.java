package com.auth.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.demo.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByName(String name);
}