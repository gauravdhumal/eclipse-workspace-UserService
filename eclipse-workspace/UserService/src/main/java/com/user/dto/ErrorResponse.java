package com.user.dto;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

	private String message;
	private int status;
	private Instant timestamp;
}
