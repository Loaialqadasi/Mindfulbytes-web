package com.project.Mental.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Mental.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	Optional<AppUser> findByUsername(String username);
}
