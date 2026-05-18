package com.user.controller;

import java.util.List;

import java.util.UUID;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.dto.requestdto.UserRequestDto;
import com.user.dto.responsedto.UserResponseDto;
import com.user.mapper.UserMapper;
import com.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/")
public class UserController {
	
	private final UserMapper userMapper;
	private final UserService userService;
	
	@PostMapping("/register")
	public UserResponseDto userCreate(@Valid @RequestBody UserRequestDto userRequestDto) 
	{
		return userMapper.toResponse(
				userService.createUser(userMapper.toEntity(userRequestDto)));

	}
	
	@PutMapping("/v1/{id}")
	public UserResponseDto userUpdate(@PathVariable UUID id,
			@RequestBody UserRequestDto userRequestDto ) {
		
		return userMapper.toResponse(
				userService.updateUser(id, userMapper.toEntity(userRequestDto)));
	}
	
	@GetMapping("/v1/{id}")
	public UserResponseDto getUserById(@PathVariable UUID id) {
		
		return userMapper.toResponse(userService.getUserById(id));
	}
	
    // Soft Delete Student
	@DeleteMapping("/v1/{id}")
	public String userDeleteById(@PathVariable UUID id) {
		
		userService.deleteUser(id);
		return "User Deleted.";
	}
	
	@PostMapping({"/bulk"})
	public List<UserResponseDto> createUsers(@RequestBody List<UserRequestDto> userRequestDtos) {
		
		return userService.bulkUsers(
				userRequestDtos.stream()
				.map(userMapper::toEntity)
				.toList()
		).stream()
		.map(userMapper::toResponse)
		.toList();
		
	}

}
