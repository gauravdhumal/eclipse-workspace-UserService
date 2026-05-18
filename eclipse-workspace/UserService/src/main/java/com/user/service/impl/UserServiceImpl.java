package com.user.service.impl;

import java.time.Instant;

import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.entity.User;
import com.user.entity.User.Role;
import com.user.entity.User.UserStatus;
import com.user.exception.BadRequestException;
import com.user.exception.ResourceNotFoundException;
import com.user.repository.UserRepository;
import com.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

//	private UserService userService;

	public User createUser(User user) {

		// Encode password
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Set default role
		if (user.getRole() == null || user.getUserStatus() == null || user.getFullName() == null) {
			user.setRole(Role.USER);
			user.setUserStatus(UserStatus.ACTIVE);
			user.setFullName(user.getFirstName() + " " + user.getLastName());
		}

		// 1. Check duplicate email
		userRepository.findByEmail(user.getEmail()).ifPresent(s -> {
			throw new BadRequestException("Email Already Exists...");
		});

		// 2. Set default values
		user.setDeletedAt(null);

		return userRepository.save(user);

	}

	// 2. Update Student
	@Override
	public User updateUser(UUID id, User updatedUser) {

		User user = userRepository.findById(id)
				. orElseThrow(() -> new ResourceNotFoundException("Student not found"));

//    	// Encode password
//		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Prevent updating deleted student
		if (user.getDeletedAt() != null) {
			throw new ResourceNotFoundException("Cannot update deleted student");
		}

		// Update fields
		user.setFirstName(updatedUser.getFirstName());
		user.setLastName(updatedUser.getLastName());
		user.setPhone(updatedUser.getPhone());
		user.setFullName(user.getFirstName() + " " + user.getLastName());

		// Email update check
		if (!user.getEmail().equals(updatedUser.getEmail())) {
			userRepository.findByEmail(updatedUser.getEmail()).ifPresent(s -> {
				throw new BadRequestException("Email already exists.");
			});
			user.setEmail(updatedUser.getEmail());
		}

		return userRepository.save(user);
	}

	// 3. Get Student by ID
	@Override
	public User getUserById(UUID id) {

		User user = userRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Useer not found"));

		if (user.getDeletedAt() != null) {
			throw new ResourceNotFoundException("User is deleted");
		}

		return user;
	}

	@Override
	public void deleteUser(UUID id) {

		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (user.getDeletedAt() != null) {
			throw new ResourceNotFoundException("User is deleted");
		}

		user.setUserStatus(UserStatus.INACTIVE);
		user.setDeletedAt(Instant.now());

		userRepository.save(user);
	}

	@Override
	public List<User> bulkUsers(List<User> user) {

		user.forEach(user1 -> {
			if (user1.getRole() == null || user1.getUserStatus() == null || user1.getFullName() == null) {
				user1.setRole(Role.USER);
				user1.setUserStatus(UserStatus.ACTIVE);
				user1.setFullName(user1.getFirstName() + " " + user1.getLastName());
				user1.setPassword(passwordEncoder.encode(user1.getPassword()));
			}

			user1.setDeletedAt(null);
		});

		List<User> listUser = user.stream().filter(s -> userRepository.findByEmail(s.getEmail()).isEmpty()).toList();

		return userRepository.saveAll(listUser);
	}

}
