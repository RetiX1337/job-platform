package com.butenov.jobplatform.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.butenov.jobplatform.users.model.User;

public interface UserRepository extends JpaRepository<User, Long>
{
	Optional<User> findByEmail(String email);
}
