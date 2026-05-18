package com.user.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.user.dto.ErrorResponse;

@RestControllerAdvice
public class GlobleExceptionHandler {

    // Handle Not Found
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException exception) 
	{
		ErrorResponse error = ErrorResponse.builder()
				.message(exception.getMessage())
				.status(HttpStatus.NOT_FOUND.value())
				.timestamp(Instant.now())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
    // Handle Bad Request
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException exception) 
	{
		ErrorResponse error = ErrorResponse.builder()
				.message(exception.getMessage())
				.status(HttpStatus.BAD_REQUEST.value())
				.timestamp(Instant.now())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		
	}
	
	 // Handle All Other Exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGloble(Exception exception) 
	{
		ErrorResponse error = ErrorResponse.builder()
				.message("Something went wrong...")
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.timestamp(Instant.now())
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
