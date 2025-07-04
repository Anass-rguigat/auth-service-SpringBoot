package com.auth.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.demo.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser , Long>{

	AppUser findByUsername(String username);
	
}
