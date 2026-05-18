package com.user.controller;

import java.util.Map;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.dto.requestdto.AuthRequest;
import com.user.entity.User;
import com.user.repository.UserRepository;
import com.user.security.JwtUtil;
import com.user.service.MailService;
import com.user.service.OtpService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	
	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	private final JwtUtil jwtUtil;
	
	private final OtpService otpService;
	
	private final MailService mailService;

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request) throws Exception {

//		System.err.println("Username: " + request.getUsername());
//		System.err.println("Password: " + request.getPassword());

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			System.err.println("Username: " + request.getUsername());
			System.err.println("Password: " + request.getPassword());

		} catch (Exception e) {
			e.printStackTrace();

//			System.err.println("Password: " + request.getPassword());
			throw new Exception("Invalid Username or Password");
		}

		String token = jwtUtil.generateToken(request.getUsername());

		return ResponseEntity.ok(Map.of("token", token));
	}

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(
	        HttpServletRequest request) {

	    // Get Authorization Header
	    String authHeader =
	            request.getHeader("Authorization");

	    // Remove Bearer
	    String token =
	            authHeader.substring(7);

	    // Extract Email From JWT
	    String email =
	            jwtUtil.extractUsername(token);

	    // Get User From DB
	    User user;
	    
	    // CHECK EMAIL OR PHONE
	    if (email.contains("@")) {
	            user = userRepository
	            		.findByEmail(email)
	                    .orElseThrow();
	            } else {
	            	// FIND BY PHONE
					user = userRepository
							.findByPhone(email)
							.orElseThrow();
	            }

	    return ResponseEntity.ok(user);
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(
	        @RequestBody Map<String, String> body) {

	    String email =
	            body.get("email");

	    User user;
	    
	    // CHECK EMAIL OR PHONE
	    if (email.contains("@")) {
	            user = userRepository
	            		.findByEmail(email)
	                    .orElseThrow(() -> new RuntimeException("User Not Found"));
	            } else {
	            	// FIND BY PHONE
					user = userRepository
							.findByPhone(email)
							.orElseThrow(() -> new RuntimeException("User Not Found"));
	            }

	    // GENERATE OTP
	    String otp =
	            String.valueOf(
	                new Random()
	                .nextInt(900000)
	                + 100000);

	    otpService.saveOtp(
	            email,
	            otp);

	    mailService.sendOtp(
	            email,
	            otp);

	    return ResponseEntity.ok(
	    		   Map.of(
	    			        "message",
	    			        "OTP Sent"
	    			    ));
	}
	
	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOtp(
	        @RequestBody Map<String, String> body) {

	    String email =
	            body.get("email");

	    String otp =
	            body.get("otp");

	    boolean valid =
	            otpService.verifyOtp(
	                    email,
	                    otp);

	    if (!valid) {

	        return ResponseEntity
	                .badRequest()
	                .body("Invalid OTP");
	    }

	    return ResponseEntity.ok(
	    		Map.of(
	    		        "message",
	    		        "OTP Verified"
	    		    ));
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(
	        @RequestBody Map<String, String> body) {

	    String email =
	            body.get("email");

	    String password =
	            body.get("password");
	    
	    String repassword =
	            body.get("password");

	    User user =
	            userRepository
	                    .findByEmail(email)
	                    .orElseThrow();

	    user.setPassword(
	            passwordEncoder.encode(password));

	    userRepository.save(user);

	    return ResponseEntity.ok(
	    		Map.of(
	    		        "message",
	    		        "Password Updated"
	    		    )
	            );
	}
}
