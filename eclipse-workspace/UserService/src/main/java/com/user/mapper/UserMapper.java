package com.user.mapper;

import org.mapstruct.Mapper;

import com.user.dto.requestdto.UserRequestDto;
import com.user.dto.responsedto.UserResponseDto;
import com.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	User toEntity(UserRequestDto userRequestDto);
	
	UserResponseDto toResponse(User user);
}
