package com.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.user.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UUID> {

//	Find by Email
	Optional<User> findByEmail(String email);
	
//	Find by phone
	Optional<User> findByPhone(String phone);
}
