package com.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class Config {
	
	private final CustomUserDetailsService customUserDetailsService;
	private final JwtFilter jwtFilter;

	public Config(CustomUserDetailsService customUserDetailsService, JwtFilter jwtFilter) {
	    this.customUserDetailsService = customUserDetailsService;
	    this.jwtFilter = jwtFilter;
	}
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		
		return http
				.csrf(csrf-> csrf.disable())
				.authorizeHttpRequests(auth-> auth
						.requestMatchers("/api/auth/**","/api/users/**").permitAll()
						.requestMatchers("/api/users/v1/**").hasAnyRole("USER")
						.anyRequest()
						.authenticated()
						)
				.authenticationProvider(AuthenticationProvider(customUserDetailsService))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
		return auth.getAuthenticationManager();	
	}
	

	@Bean
	public DaoAuthenticationProvider AuthenticationProvider(CustomUserDetailsService customUserDetailsService) {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}
}
