package com.auth.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.demo.entity.AppRole;

public interface AppRoleRepository extends JpaRepository<AppRole , Long>{

	AppRole findByRoleName(String roleName);
}
