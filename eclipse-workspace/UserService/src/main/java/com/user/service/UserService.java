package com.user.service;

import java.util.List;
import java.util.UUID;

import com.user.entity.User;

public interface UserService {

	User createUser(User user);
	
	User updateUser(UUID id, User user);
	
	User getUserById(UUID id);
	
	void deleteUser(UUID id);
	
	List<User> bulkUsers(List<User> user);

}
