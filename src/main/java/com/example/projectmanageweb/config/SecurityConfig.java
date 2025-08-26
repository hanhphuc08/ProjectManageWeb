package com.example.projectmanageweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/",
					"/index",
					"/favicon.ico",
					"/css/**",
					"/js/**",
					"/images/**",
					"/vendors/**",
					"/assets/**",
					"/webjars/**"
				).permitAll()
				.anyRequest().permitAll()
			)
			.csrf(csrf -> csrf.disable())
			.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
		return http.build();
	}
} 