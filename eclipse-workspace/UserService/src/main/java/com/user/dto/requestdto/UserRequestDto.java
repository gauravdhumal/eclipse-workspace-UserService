package com.user.dto.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {

	@NotBlank(message = "Frist Name is Required")
	private String firstName;

	@NotBlank(message = "Last Name is Required")
	private String lastName;

	@Email(message = "Invalid Email format")
	@NotBlank(message = "Email is required")
	private String email;

	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 Digit")
	private String phone;

	@Size(min = 6, message = "Password must be at least 6 characters")
	@NotBlank(message = "Password is required")
	private String password;
	
	
}
